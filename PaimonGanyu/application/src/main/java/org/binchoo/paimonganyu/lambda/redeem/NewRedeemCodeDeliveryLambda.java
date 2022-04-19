package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimation;
import org.binchoo.paimonganyu.redeem.options.RedeemAllUsers;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
public class NewRedeemCodeDeliveryLambda {

    private static final String CODEREDEEM_QUEUE_URL = System.getenv("CODEREDEEM_QUEUE_URL");

    private AmazonSQS sqsClient;
    private AmazonS3 s3Client;
    private ObjectMapper objectMapper;
    private RedeemTaskEstimation redeemTaskEstimation;
    private UserHoyopassCrudPort userHoyopassCrudPort;

    public NewRedeemCodeDeliveryLambda() {
//        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckHitoriRequesterMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
    }

    public void handler(S3Event s3Event) {
        List<RedeemCode> newRedeemCodes = new S3EventObjectReader(s3Event, s3Client).extractPojos(RedeemCode.class);
        sendTasks(generateRedeemTasks(newRedeemCodes));
    }

    private List<RedeemTask> generateRedeemTasks(List<RedeemCode> codes) {
        return redeemTaskEstimation.generateTasks(new RedeemAllUsers(userHoyopassCrudPort, codes));
    }

    private void sendTasks(List<RedeemTask> tasks) {
        tasks.forEach(task-> sqsClient.sendMessage(CODEREDEEM_QUEUE_URL, task.getJson(objectMapper)));
    }
}
