package org.binchoo.paimonganyu.hoyoapi.webclient.async;

import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyoapi.pojo.AccountIdCookieToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author : jbinchoo
 * @since : 2022-04-23
 */
@SpringJUnitConfig(HoyoApiWebClientConfigurer.class)
class CodeRedemptionWebClientTest {

    @Autowired
    CodeRedemptionWebClient codeRedemption;

    @Test
    void givenInvalidArguments_redeem_fails() {
        var responseMono = codeRedemption.redeem(
                new AccountIdCookieToken("foo", "bar"),
                "foobar", "os_asia", "foobar");
        assertThrows(Exception.class, ()-> responseMono.block());
    }
}