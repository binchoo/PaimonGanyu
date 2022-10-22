package org.binchoo.paimonganyu.hoyoapi.webclient.async;

import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author : jbinchoo
 * @since : 2022-04-23
 */
@SpringJUnitConfig(HoyoApiWebClientConfigurer.class)
class DailyCheckAsyncWebClientTest {

    @Autowired
    DailyCheckAsyncWebClient client;

    @Test
    void givenInvalidArguments_dailyCheck_fails() {
        var badLtuidLtoken = new LtuidLtoken("foo", "bar");
        var responseMono = client.claimDailyCheck(badLtuidLtoken);
        assertThrows(RetcodeException.class, ()-> responseMono.onErrorMap(Throwable::getCause).block());
    }
}
