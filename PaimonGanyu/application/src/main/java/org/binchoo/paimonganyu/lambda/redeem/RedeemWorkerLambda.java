package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.support.AwsEventWrapperFactory;
import org.binchoo.paimonganyu.lambda.RedeemWorkerMain;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driving.RedemptionService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-21
 */
@Slf4j
public class RedeemWorkerLambda {

    private RedemptionService codeRedeemService = null;

    public RedeemWorkerLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(RedeemWorkerMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.codeRedeemService = context.getBean(RedemptionService.class);
    }

    public void handler(SQSEvent sqsEvent) {
        var eventWrapper = AwsEventWrapperFactory.getWrapper(sqsEvent);
        List<RedeemTask> tasks = eventWrapper.extractPojos(sqsEvent, RedeemTask.class);
        List<UserRedeem> results = codeRedeemService.redeem(tasks);
        log.info("Code Redemption Result: {}", results);
    }
}
