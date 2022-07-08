package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.support.AwsEventWrapperFactory;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.lambda.DailyCheckHitoriRequesterMain;
import org.binchoo.paimonganyu.lambda.dailycheck.dto.UserHoyopassMessage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.Objects;

public class DailyCheckHitoriRequesterLambda {

    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private DailyCheckPort dailyCheckPort;

    public DailyCheckHitoriRequesterLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckHitoriRequesterMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.dailyCheckPort = Objects.requireNonNull(context.getBean(DailyCheckPort.class));
    }

    public void handler(SNSEvent snsEvent) {
        var factory = AwsEventWrapperFactory.getDefault();
        var eventWrapper = factory.newWrapper(snsEvent);
        eventWrapper.extractPojos(snsEvent, UserHoyopassMessage.class)
                .stream().map(DailyCheckTaskSpec::specify)
                .flatMap(List::stream)
                .filter(this::ifNotDoneToday)
                .forEach(this::sendToQueue);
    }

    private boolean ifNotDoneToday(DailyCheckTaskSpec task) {
        return !dailyCheckPort.hasCheckedInToday(task.getBotUserId(), task.getLtuid());
    }

    private void sendToQueue(DailyCheckTaskSpec task) {
        String json = task.asJson(objectMapper);
        sqsClient.sendMessage(DAILYCHECK_QUEUE_URL, json);
    }
}
