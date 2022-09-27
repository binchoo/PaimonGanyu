package org.binchoo.paimonganyu.lambda.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.binchoo.paimonganyu.awsutils.support.AwsEventParserFactory;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.lambda.DailyCheckWorkerMain;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;

public class DailyCheckWorkerLambda {

    private static final long RETRY_SLEEP = 3000;

    private DailyCheckPort dailyCheckPort;

    public DailyCheckWorkerLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(DailyCheckWorkerMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.dailyCheckPort = Objects.requireNonNull(context.getBean(DailyCheckPort.class));
    }

    public void handler(SQSEvent event) {
        var factory = AwsEventParserFactory.getDefault();
        var eventParser = factory.newParser(event);
        eventParser.extractPojos(event, DailyCheckTaskSpec.class).forEach(this::claim);
    }

    /**
     * @throws RuntimeException - When a UserDailyCheck is not successful, report an error to the AWS Lambda service.
     */
    private void claim(DailyCheckTaskSpec taskSpec) {
        String botUserId = taskSpec.getBotUserId(), ltuid = taskSpec.getLtuid(), ltoken = taskSpec.getLtoken();
        UserDailyCheck userDailyCheck = dailyCheckPort.claimDailyCheckIn(botUserId, ltuid, ltoken);
        if (!userDailyCheck.isDone()) {
            try {
                Thread.sleep(RETRY_SLEEP);
            } catch (Exception ignored) {
            }
            throw new RuntimeException();
        }
    }
}
