package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
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
    private AmazonCloudWatch cloudWatchClient;
    private ObjectMapper objectMapper;
    private DailyCheckPort dailyCheck;
    private UserHoyopassCrudPort hoyopassCrud;

    public DailyCheckBatchRequesterLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckBatchRequesterMain.class));
        this.putSuccessRate();
    }

    private void putSuccessRate() {
        MetricDatum metricData = new MetricDatum()
                .withMetricName("success_rate")
                .withUnit(StandardUnit.Percent)
                .withValue(dailyCheck.getCheckedInRate()*100);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("paimonganyu/dailycheck")
                .withMetricData(metricData);
        cloudWatchClient.putMetricData(request);
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.cloudWatchClient = context.getBean(AmazonCloudWatch.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.dailyCheck = Objects.requireNonNull(context.getBean(DailyCheckPort.class));;
        this.hoyopassCrud = Objects.requireNonNull(context.getBean(UserHoyopassCrudPort.class));;
    }

    public void handler(ScheduledEvent event) {
        hoyopassCrud.findAll().stream().map(DailyCheckTaskSpec::specify)
                .flatMap(List::stream)
                .filter(task-> !dailyCheck.hasCheckedInToday(task.getBotUserId(), task.getLtuid()))
                .forEach(task-> sqsClient.sendMessage(DAILYCHECK_QUEUE_URL, task.asJson(objectMapper)));
    }
}
