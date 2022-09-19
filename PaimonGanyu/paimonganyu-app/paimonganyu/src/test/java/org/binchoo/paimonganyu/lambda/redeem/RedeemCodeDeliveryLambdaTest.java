package org.binchoo.paimonganyu.lambda.redeem;

import net.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("loadProvider")
    void taskSplit(int n, int batchSize) {
        int expectedBatches = (n%batchSize == 0)? n/batchSize : (n/batchSize + 1);
        List<RedeemTask> tasks = randomTasks(n);

        var deliveryLambda = new RedeemCodeDeliveryLambda();
        var messageBatch = deliveryLambda.taskSplit(tasks, batchSize);

        assertThat(messageBatch).hasSize(expectedBatches);
        System.out.println(messageBatch);
    }

    private static Stream<Arguments> loadProvider() {
        Stream.Builder<Arguments> streamBuilder = Stream.builder();
        for (int batchSize = 1; batchSize <= 10; batchSize++)
            streamBuilder.add(Arguments.of(100, batchSize));
        return streamBuilder.build();
    }

    private List<RedeemTask> randomTasks(int n) {
        List<RedeemTask> tasks = new ArrayList<>();
        while (n--> 0)
            tasks.add(RedeemTask.builder()
                    .botUserId(RandomString.make())
                    .redeemCode(RedeemCode.of(RandomString.make()))
                    .reason("load test")
                    .build());
        return tasks;
    }
}
