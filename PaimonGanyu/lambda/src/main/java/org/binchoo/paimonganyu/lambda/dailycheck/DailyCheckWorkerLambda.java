package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.binchoo.paimonganyu.awsutils.sqs.SQSEventWrapper;
import org.binchoo.paimonganyu.dailycheck.service.DailyCheckService;
import org.binchoo.paimonganyu.lambda.config.DailyCheckConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;

public class DailyCheckWorkerLambda {

    private DailyCheckService dailyCheckService;

    public DailyCheckWorkerLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckConfig.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.dailyCheckService = context.getBean(DailyCheckService.class);
        Objects.requireNonNull(dailyCheckService);
        Objects.requireNonNull(dailyCheckService.getRepository());
    }

    public void handler(SQSEvent event) {
        new SQSEventWrapper(event).extractPojos(DailyCheckTaskSpec.class)
                .forEach(taskSpec -> dailyCheckService
                        .claimDailyCheckIn(taskSpec.getBotUserId(), taskSpec.getLtuid(), taskSpec.getLtoken()));
    }
}
