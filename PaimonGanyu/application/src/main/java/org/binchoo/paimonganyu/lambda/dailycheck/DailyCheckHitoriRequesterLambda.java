package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.sns.SNSEventWrapper;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckService;
import org.binchoo.paimonganyu.lambda.dailycheck.dto.UserHoyopassMessage;
import org.binchoo.paimonganyu.lambda.DailyCheckHitoriRequesterMain;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.Objects;

public class DailyCheckHitoriRequesterLambda {

    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private DailyCheckService dailyCheckService;

    public DailyCheckHitoriRequesterLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckHitoriRequesterMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.dailyCheckService = context.getBean(DailyCheckService.class);
        Objects.requireNonNull(dailyCheckService);
    }

    public void handler(SNSEvent snsEvent) {
        new SNSEventWrapper(snsEvent).extractPojos(UserHoyopassMessage.class).stream()
                .map(DailyCheckTaskSpec::getList).flatMap(List::stream)
                .filter(task-> !dailyCheckService.hasCheckedInToday(task.getBotUserId(), task.getLtuid()))
                .forEach(task-> sqsClient.sendMessage(DAILYCHECK_QUEUE_URL, task.getJson(objectMapper)));
    }
}
