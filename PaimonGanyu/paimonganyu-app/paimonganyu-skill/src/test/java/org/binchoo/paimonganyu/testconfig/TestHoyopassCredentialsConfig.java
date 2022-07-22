package org.binchoo.paimonganyu.testconfig;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author : jbinchoo
 * @since : 2022/04/16
 */
@Configuration
@PropertySource("classpath:accounts.properties")
public class TestHoyopassCredentialsConfig {

    @Bean
    public TestCredentials valid0(@Value("${valid.ltuid}") String ltuid,
                                  @Value("${valid.ltoken}") String ltoken,
                                  @Value("${valid.cookietoken}") String cookieToken){
        return new TestCredentials(ltuid, ltoken, cookieToken);
    }

    @Bean
    public TestCredentials valid1(@Value("${valid2.ltuid}") String ltuid,
                                  @Value("${valid2.ltoken}") String ltoken,
                                  @Value("${valid2.cookietoken}") String cookieToken){
        return new TestCredentials(ltuid, ltoken, cookieToken);
    }

    @Bean
    public InvalidTestCredentials invalid(){
        return new InvalidTestCredentials("foo", "bar", "abc");
    }

    public static class TestCredentials extends LtuidLtoken {
        String cookieToken;
        public TestCredentials(String ltuid, String ltoken, String cookieToken) {
            super(ltuid, ltoken);
            this.cookieToken = cookieToken;
        }
        public String getCookieToken() {
            return cookieToken;
        }
    }

    public static class InvalidTestCredentials extends TestCredentials {
        public InvalidTestCredentials(String ltuid, String ltoken, String cookieToken) {
            super(ltuid, ltoken, cookieToken);
        }
    }
}
