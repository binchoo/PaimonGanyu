package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.binchoo.paimonganyu.awsutils.support.AwsEventWrapperFactory;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.lambda.DailyCheckWorkerMain;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;

public class DailyCheckWorkerLambda {

    private DailyCheckPort dailyCheckPort;

    public DailyCheckWorkerLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckWorkerMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.dailyCheckPort = context.getBean(DailyCheckPort.class);
        Objects.requireNonNull(dailyCheckPort);
    }

    public void handler(SQSEvent event) {
        var factory = AwsEventWrapperFactory.getDefault();
        var eventWrapper = factory.newWrapper(event);
        eventWrapper.extractPojos(event, DailyCheckTaskSpec.class)
                .forEach(taskSpec -> dailyCheckPort
                        .claimDailyCheckIn(taskSpec.getBotUserId(), taskSpec.getLtuid(), taskSpec.getLtoken()));
    }
}
