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
public class TestLtuidLtokenConfig {

    @Bean
    public ValidLtuidLtoken valid0(@Value("${valid.ltuid}") String ltuid,
                                              @Value("${valid.ltoken}") String ltoken){
        return new ValidLtuidLtoken(ltuid, ltoken);
    }

    @Bean
    public ValidLtuidLtoken valid1(@Value("${valid2.ltuid}") String ltuid,
                                              @Value("${valid2.ltoken}") String ltoken){
        return new ValidLtuidLtoken(ltuid, ltoken);
    }

    @Bean
    public InvalidLtuidLtoken invalidLtuidLtoken(){
        return new InvalidLtuidLtoken("foo", "bar");
    }

    public static class ValidLtuidLtoken extends LtuidLtoken {

        public ValidLtuidLtoken(String ltuid, String ltoken) {
            super(ltuid, ltoken);
        }
    }

    public static class InvalidLtuidLtoken extends LtuidLtoken {

        public InvalidLtuidLtoken(String ltuid, String ltoken) {
            super(ltuid, ltoken);
        }
    }
}
