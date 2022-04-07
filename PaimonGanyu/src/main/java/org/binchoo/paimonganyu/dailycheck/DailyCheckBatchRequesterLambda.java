package org.binchoo.paimonganyu.dailycheck;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.config.DailyCheckConfig;
import org.binchoo.paimonganyu.dailycheck.service.DailyCheckService;
import org.binchoo.paimonganyu.hoyopass.domain.driven.UserHoyopassCrudPort;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.Objects;

public class DailyCheckBatchRequesterLambda {

    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private DailyCheckService dailyCheckService;
    private UserHoyopassCrudPort crudPort;

    public DailyCheckBatchRequesterLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckConfig.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.dailyCheckService = context.getBean(DailyCheckService.class);
        this.crudPort = context.getBean(UserHoyopassCrudPort.class);
        Objects.requireNonNull(dailyCheckService);
        Objects.requireNonNull(dailyCheckService.getUserDailyCheckRepository());
        Objects.requireNonNull(crudPort);
    }

    public void handler(ScheduledEvent event, Context context) {
        crudPort.findAll().stream().map(DailyCheckTaskSpec::getList)
                .flatMap(List::stream)
                .filter(task-> dailyCheckService.hasCheckedInToday(task.getBotUserId(), task.getLtuid()))
                .forEach(task-> sqsClient.sendMessage(DAILYCHECK_QUEUE_URL, task.getJson(objectMapper)));
    }
}
