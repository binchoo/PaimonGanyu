package org.binchoo.paimonganyu.lambda.redeem;

import net.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
class RedeemCodeDeliveryLambdaTest {

    @Test
    void bootstrap() {
        new RedeemCodeDeliveryLambda();
    }

    @Test
    void taskSplit() {
        int n = 101, batchSize = 10;
        int expectedBatches = (n % batchSize == 0)? n / batchSize : (n / batchSize + 1);
        var tasks = task(n);

        var codeDelivery = new RedeemCodeDeliveryLambda();
        var batch = codeDelivery.taskSplit(tasks, batchSize);

        assertThat(batch).hasSize(expectedBatches);
        System.out.println(batch);
    }

    private List<RedeemTask> task(int n) {
        List<RedeemTask> tasks = new ArrayList<>();
        while (n--> 0)
            tasks.add(RedeemTask.builder()
                    .botUserId(RandomString.make())
                    .redeemCode(new RedeemCode(RandomString.make()))
                    .build());
        return tasks;
    }
}
