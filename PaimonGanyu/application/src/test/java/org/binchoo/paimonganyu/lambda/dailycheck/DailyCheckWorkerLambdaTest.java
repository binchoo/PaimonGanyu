package org.binchoo.paimonganyu.lambda.dailycheck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : jbinchoo
 * @since : 2022-04-13
 */
class DailyCheckWorkerLambdaTest {

    @Test
    void bootstrap() {
        new DailyCheckWorkerLambda();
    }
}