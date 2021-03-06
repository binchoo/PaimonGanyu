package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.support.AwsEventParserFactory;
import org.binchoo.paimonganyu.lambda.RedeemUserDeliveryMain;
import org.binchoo.paimonganyu.lambda.dailycheck.dto.UserHoyopassMessage;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationPort;
import org.binchoo.paimonganyu.service.redeem.RedeemAllCodesOption;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

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
    private RedeemTaskEstimationPort redeemTaskEstimation;
    private RedeemCodeCrudPort redeemCodeCrud;

    public RedeemUserDeliveryLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(RedeemUserDeliveryMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.redeemTaskEstimation = Objects.requireNonNull(context.getBean(RedeemTaskEstimationPort.class));
        this.redeemCodeCrud = Objects.requireNonNull(context.getBean(RedeemCodeCrudPort.class));
    }

    public void handler(SNSEvent snsEvent) {
        var factory = AwsEventParserFactory.getDefault();
        var eventParser = factory.newParser(snsEvent);
        var users = eventParser.extractPojos(snsEvent, UserHoyopassMessage.class);
        RedeemTaskEstimationOption estimationOption = new RedeemAllCodesOption(redeemCodeCrud, ()-> users.stream()
                    .map(UserHoyopassMessage::toDomain)
                    .collect(Collectors.toList()));
        sendToQueue(redeemTaskEstimation.generateTasks(estimationOption));
    }

    private void sendToQueue(List<RedeemTask> redeemTasks) {
        for (RedeemTask task : redeemTasks) {
            sqsClient.sendMessage(CODEREDEEM_QUEUE_NAME, task.getJson(objectMapper));
        }
    }
}
