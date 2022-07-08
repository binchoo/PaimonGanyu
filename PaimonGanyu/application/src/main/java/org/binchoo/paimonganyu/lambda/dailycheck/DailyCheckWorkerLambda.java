package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;
import org.binchoo.paimonganyu.awsutils.support.AwsEventWrapperFactory;
import org.binchoo.paimonganyu.awsutils.support.template.AsyncEventWrappingLambda;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.lambda.DailyCheckWorkerMain;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;

public class DailyCheckWorkerLambda extends AsyncEventWrappingLambda<SQSEvent> {

    private DailyCheckPort dailyCheckPort;

    @Override
    protected void lookupDependencies() {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(DailyCheckWorkerMain.class);
        this.dailyCheckPort = Objects.requireNonNull(context.getBean(DailyCheckPort.class));

    }

    @Override
    protected void doHandle(SQSEvent event, AwsEventWrapper<SQSEvent> eventWrapper) {
        eventWrapper.extractPojos(event, DailyCheckTaskSpec.class)
                .forEach(taskSpec -> dailyCheckPort
                        .claimDailyCheckIn(taskSpec.getBotUserId(), taskSpec.getLtuid(), taskSpec.getLtoken()));
    }
}
