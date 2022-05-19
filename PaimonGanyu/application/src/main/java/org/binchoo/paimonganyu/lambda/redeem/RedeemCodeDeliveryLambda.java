package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.lambda.RedeemCodeDeliveryMain;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationService;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;
import org.binchoo.paimonganyu.service.redeem.RedeemAllUsersOption;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
public class RedeemCodeDeliveryLambda {

    private static final String CODEREDEEM_QUEUE_NAME = System.getenv("CODEREDEEM_QUEUE_NAME");

    private AmazonS3 s3Client;
    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private RedeemTaskEstimationService redeemTaskEstimationService;
    private UserHoyopassCrudPort userHoyopassCrudPort;

    public RedeemCodeDeliveryLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(RedeemCodeDeliveryMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.s3Client = context.getBean(AmazonS3.class);
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.redeemTaskEstimationService = context.getBean(RedeemTaskEstimationService.class);
        this.userHoyopassCrudPort = context.getBean(UserHoyopassCrudPort.class);
        Objects.requireNonNull(this.redeemTaskEstimationService);
        Objects.requireNonNull(this.userHoyopassCrudPort);
    }

    public void handler(S3Event s3Event) {
        var eventWrapper = new S3EventObjectReader(s3Client);
        var redeemCodeList = eventWrapper.extractPojos(s3Event, RedeemCode.class);
        RedeemTaskEstimationOption estimationOption = new RedeemAllUsersOption(userHoyopassCrudPort,
                ()-> Collections.unmodifiableList(redeemCodeList));
        sendToQueue(redeemTaskEstimationService.generateTasks(estimationOption));
    }

    private void sendToQueue(List<RedeemTask> redeemTasks) {
        List<SendMessageBatchRequestEntry> batchMessage = new LinkedList<>();
        for (int i = 0; i < redeemTasks.size(); i++) {
            batchMessage.add(new SendMessageBatchRequestEntry()
                    .withId(String.valueOf(i))
                    .withMessageBody(redeemTasks.get(i).getJson(objectMapper)));
        }
        sqsClient.sendMessageBatch(CODEREDEEM_QUEUE_NAME, batchMessage);
    }
}
