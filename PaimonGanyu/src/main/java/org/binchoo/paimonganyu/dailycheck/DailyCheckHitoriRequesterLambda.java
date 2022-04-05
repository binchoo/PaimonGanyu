package org.binchoo.paimonganyu.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.sns.SNSEventWrapper;
import org.binchoo.paimonganyu.dailycheck.config.DailyCheckLambdaConfig;
import org.binchoo.paimonganyu.dailycheck.service.DailyCheckService;
import org.binchoo.paimonganyu.hoyopass.infra.fanout.UserHoyopassMessage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class DailyCheckHitoriRequesterLambda {

    private static final String CONTEXT_PROFILE = "dailycheck";
    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;
    private final DailyCheckService dailyCheckService;

    public DailyCheckHitoriRequesterLambda() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().addActiveProfile(CONTEXT_PROFILE);
        context.register(DailyCheckLambdaConfig.class);
        context.refresh();

        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.dailyCheckService = context.getBean(DailyCheckService.class);
    }

    public void handler(SNSEvent snsEvent) {
        new SNSEventWrapper(snsEvent).extractPojos(UserHoyopassMessage.class).stream()
                .map(DailyCheckTaskSpec::getList).flatMap(List::stream)
                .filter(task-> !task.isDoneToday(dailyCheckService))
                .forEach(task-> task.sendToQueue(sqsClient, DAILYCHECK_QUEUE_URL, objectMapper));
    }
}
