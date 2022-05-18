package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.awsutils.support.AwsEventWrapperFactory;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.lambda.RedeemCodeDeliveryMain;
import org.binchoo.paimonganyu.lambda.RedeemUserDeliveryMain;
import org.binchoo.paimonganyu.lambda.dailycheck.dto.UserHoyopassMessage;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationService;
import org.binchoo.paimonganyu.redeem.options.RedeemAllCodesOption;
import org.binchoo.paimonganyu.redeem.options.RedeemAllUsersOption;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
public class RedeemUserDeliveryLambda {

    private static final String CODEREDEEM_QUEUE_NAME = System.getenv("CODEREDEEM_QUEUE_NAME");

    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private RedeemTaskEstimationService redeemTaskEstimationService;
    private RedeemCodeCrudPort redeemCodeCrudPort;

    public RedeemUserDeliveryLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(RedeemUserDeliveryMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.redeemTaskEstimationService = context.getBean(RedeemTaskEstimationService.class);
        this.redeemCodeCrudPort = context.getBean(RedeemCodeCrudPort.class);
        Objects.requireNonNull(this.redeemTaskEstimationService);
        Objects.requireNonNull(this.redeemCodeCrudPort);
    }

    public void handler(SNSEvent snsEvent) {
        var eventWrapper = AwsEventWrapperFactory.getWrapper(snsEvent);
        var users = eventWrapper.extractPojos(snsEvent, UserHoyopassMessage.class);
        RedeemTaskEstimationOption estimationOption = new RedeemAllCodesOption(redeemCodeCrudPort, ()-> users.stream()
                    .map(UserHoyopassMessage::toDomain)
                    .collect(Collectors.toList()));
        sendToQueue(redeemTaskEstimationService.generateTasks(estimationOption));
    }

    private void sendToQueue(List<RedeemTask> redeemTasks) {
        for (RedeemTask task : redeemTasks) {
            sqsClient.sendMessage(CODEREDEEM_QUEUE_NAME, task.getJson(objectMapper));
        }
    }
}
