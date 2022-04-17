package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.binchoo.paimonganyu.awsutils.sqs.SQSEventWrapper;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckService;
import org.binchoo.paimonganyu.lambda.DailyCheckWorkerMain;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;

public class DailyCheckWorkerLambda {

    private DailyCheckService dailyCheckService;

    public DailyCheckWorkerLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckWorkerMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.dailyCheckService = context.getBean(DailyCheckService.class);
        Objects.requireNonNull(dailyCheckService);
    }

    public void handler(SQSEvent event) {
        new SQSEventWrapper(event).extractPojos(DailyCheckTaskSpec.class)
                .forEach(taskSpec -> dailyCheckService
                        .claimDailyCheckIn(taskSpec.getBotUserId(), taskSpec.getLtuid(), taskSpec.getLtoken()));
    }
}
