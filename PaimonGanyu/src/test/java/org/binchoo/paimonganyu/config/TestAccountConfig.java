package org.binchoo.paimonganyu.config;

import org.binchoo.paimonganyu.TestAccountDetails;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:accounts.properties")
public class TestAccountConfig {

    private List<LtuidLtoken> testAccounts = new ArrayList<>();

    @Bean("validHoyopass")
    LtuidLtoken validAccount(@Value("${valid.ltuid}") String ltuid, @Value("${valid.ltoken}") String ltoken) {
        return new LtuidLtoken(ltuid, ltoken);
    }

    @Bean("validHoyopass2")
    LtuidLtoken valid2Account(@Value("${valid2.ltuid}") String ltuid, @Value("${valid2.ltoken}") String ltoken) {
        return new LtuidLtoken(ltuid, ltoken);
    }

    @Bean("fakeHoyopass")
    LtuidLtoken fakeAccount() {
        return new LtuidLtoken("111", "zzz");
    }


    @Bean({"aetherAccountDetails", "asiaAccountDetails"})
    TestAccountDetails aetherAccountDetails(@Value("${aether.ltuid}") String ltuid, @Value("${aether.ltoken}") String ltoken,
                                            @Value("${aether.region}") String region, @Value("${aether.uid}") String uid) {

        return TestAccountDetails.builder()
                .ltuidLtoken(new LtuidLtoken(ltuid, ltoken))
                .region(region).uid(uid)
                .build();
    }

    @Bean({"lumineAccountDetails", "usaAccountDetails"})
    TestAccountDetails lumineAccountDetails(@Value("${lumine.ltuid}") String ltuid, @Value("${lumine.ltoken}") String ltoken,
                                            @Value("${lumine.region}") String region, @Value("${lumine.uid}") String uid) {

        return TestAccountDetails.builder()
                .ltuidLtoken(new LtuidLtoken(ltuid, ltoken))
                .region(region).uid(uid)
                .build();
    }
}
