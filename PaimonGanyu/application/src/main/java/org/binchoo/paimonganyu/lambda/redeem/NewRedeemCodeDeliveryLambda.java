package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.lambda.NewRedeemCodeDeliveryMain;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationService;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;
import org.binchoo.paimonganyu.service.redeem.RedeemAllUsersOption;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.Objects;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
public class NewRedeemCodeDeliveryLambda {

    private static final String REDEEM_QUEUE_URL = System.getenv("REDEEM_QUEUE_URL");

    private AmazonSQS sqsClient;
    private AmazonS3 s3Client;
    private ObjectMapper objectMapper;
    private RedeemTaskEstimationService redeemTaskEstimationService;
    private UserHoyopassCrudPort userHoyopassCrudPort;

    public NewRedeemCodeDeliveryLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(NewRedeemCodeDeliveryMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.s3Client = context.getBean(AmazonS3.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.redeemTaskEstimationService = context.getBean(RedeemTaskEstimationService.class);
        this.userHoyopassCrudPort = context.getBean(UserHoyopassCrudPort.class);
        Objects.requireNonNull(this.redeemTaskEstimationService);
        Objects.requireNonNull(this.userHoyopassCrudPort);
    }

    public void handler(S3Event s3Event) {
        List<RedeemCode> newRedeemCodes = new S3EventObjectReader(s3Event, s3Client).extractPojos(RedeemCode.class);
        sendTasks(generateRedeemTasks(newRedeemCodes));
    }

    private List<RedeemTask> generateRedeemTasks(List<RedeemCode> codes) {
        return redeemTaskEstimationService.generateTasks(new RedeemAllUsersOption(userHoyopassCrudPort, codes));
    }

    private void sendTasks(List<RedeemTask> tasks) {
        tasks.forEach(task-> sqsClient.sendMessage(REDEEM_QUEUE_URL, task.getJson(objectMapper)));
    }
}
