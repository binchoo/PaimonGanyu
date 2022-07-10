package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.support.AwsEventParserFactory;
import org.binchoo.paimonganyu.lambda.RedeemWorkerMain;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driving.RedemptionPort;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-21
 */
@Slf4j
public class RedeemWorkerLambda {

    private RedemptionPort codeRedeemService = null;

    public RedeemWorkerLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(RedeemWorkerMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.codeRedeemService = context.getBean(RedemptionPort.class);
    }

    public void handler(SQSEvent sqsEvent) {
        var factory = AwsEventParserFactory.getDefault();
        var eventParser = factory.newParser(sqsEvent);
        List<RedeemTask> tasks = eventParser.extractPojos(sqsEvent, RedeemTask.class);
        List<UserRedeem> results = codeRedeemService.redeem(tasks);
        log.info("Code Redemption Result: {}", results);
    }
}
