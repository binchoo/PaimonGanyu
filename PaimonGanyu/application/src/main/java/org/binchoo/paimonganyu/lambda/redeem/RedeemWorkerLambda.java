package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.binchoo.paimonganyu.awsutils.support.template.AsyncEventWrappingLambda;
import org.binchoo.paimonganyu.lambda.RedeemWorkerMain;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driving.RedemptionPort;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.Objects;

/**
 * @author : jbinchoo
 * @since : 2022-04-21
 */
@Slf4j
public class RedeemWorkerLambda extends AsyncEventWrappingLambda<SQSEvent> {

    private RedemptionPort codeRedeemService;

    @Override
    protected void lookupDependencies() {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(RedeemWorkerMain.class);
        this.codeRedeemService = Objects.requireNonNull(context.getBean(RedemptionPort.class));
    }

    @Override
    protected void doHandle(SQSEvent event, AwsEventParser<SQSEvent> eventParser) {
        List<RedeemTask> tasks = eventParser.extractPojos(event, RedeemTask.class);
        List<UserRedeem> results = codeRedeemService.redeem(tasks);
        log.info("Code Redemption Result: {}", results);
    }
}
