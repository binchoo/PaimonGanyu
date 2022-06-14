package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.lambda.DailyCheckBatchRequesterMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.Objects;

public class DailyCheckBatchRequesterLambda {

    private static final Logger logger = LoggerFactory.getLogger(DailyCheckBatchRequesterLambda.class);
    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private DailyCheckPort dailyCheckPort;
    private UserHoyopassCrudPort hoyopassCrudPort;

    public DailyCheckBatchRequesterLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckBatchRequesterMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.dailyCheckPort = context.getBean(DailyCheckPort.class);
        this.hoyopassCrudPort = context.getBean(UserHoyopassCrudPort.class);
        Objects.requireNonNull(dailyCheckPort);
        Objects.requireNonNull(hoyopassCrudPort);
    }

    public void handler(ScheduledEvent event) {
        logger.info("SchedueldEvent triggered at {}.", event.getTime());
        hoyopassCrudPort.findAll().stream().map(DailyCheckTaskSpec::specify)
                .flatMap(List::stream)
                .filter(task-> !dailyCheckPort.hasCheckedInToday(task.getBotUserId(), task.getLtuid()))
                .forEach(task-> sqsClient.sendMessage(DAILYCHECK_QUEUE_URL, task.asJson(objectMapper)));
    }
}
