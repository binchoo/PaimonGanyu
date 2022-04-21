package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.sqs.SQSEventWrapper;
import org.binchoo.paimonganyu.redeem.RedeemResult;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driving.RedeemerService;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-21
 */
@Slf4j
public class RedeemWorkerLambda {

    private RedeemerService codeRedeemService = null;

    public void handler(SQSEvent sqsEvent) {
        List<RedeemTask> tasks = new SQSEventWrapper(sqsEvent).extractPojos(RedeemTask.class);
        List<RedeemResult> results = codeRedeemService.redeem(tasks);
        log.info("RedeemResults: {}", results);
    }
}
