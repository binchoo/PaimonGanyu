package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.binchoo.paimonganyu.awsutils.support.template.AsyncEventWrappingLambda;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.lambda.DailyCheckHitoriRequesterMain;
import org.binchoo.paimonganyu.lambda.dailycheck.dto.UserHoyopassMessage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.Objects;

public class DailyCheckHitoriRequesterLambda extends AsyncEventWrappingLambda<SNSEvent> {

    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private DailyCheckPort dailyCheckPort;

    @Override
    protected void lookupDependencies() {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(DailyCheckHitoriRequesterMain.class);
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.dailyCheckPort = Objects.requireNonNull(context.getBean(DailyCheckPort.class));
    }

    @Override
    protected void doHandle(SNSEvent event, AwsEventParser<SNSEvent> eventParser) {
        eventParser.extractPojos(event, UserHoyopassMessage.class)
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
