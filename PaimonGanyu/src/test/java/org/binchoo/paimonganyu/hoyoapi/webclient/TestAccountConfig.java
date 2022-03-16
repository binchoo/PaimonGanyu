package org.binchoo.paimonganyu.hoyoapi.webclient;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@PropertySource("classpath:accounts.properties")
@Configuration
public class TestAccountConfig {

    private List<LtuidLtoken> testAccounts = new ArrayList<>();

    @Bean("validAccount")
    LtuidLtoken validAccount(@Value("${valid.ltuid}") String ltuid, @Value("${valid.ltoken}") String ltoken) {
        return new LtuidLtoken(ltuid, ltoken);
    }

    @Bean("asiaAccount")
    LtuidLtoken asiaAccount(@Value("${asia.ltuid}") String ltuid, @Value("${asia.ltoken}") String ltoken) {
        return new LtuidLtoken(ltuid, ltoken);

    }

    @Bean("usaAccount")
    LtuidLtoken usaAccount(@Value("${usa.ltuid}") String ltuid, @Value("${usa.ltoken}") String ltoken) {
        return new LtuidLtoken(ltuid, ltoken);

    }

    @Bean("aetherAccount")
    LtuidLtoken aetherAccount(@Value("${aether.ltuid}") String ltuid, @Value("${aether.ltoken}") String ltoken) {
        return new LtuidLtoken(ltuid, ltoken);

    }

    @Bean("lumineAccount")
    LtuidLtoken lumineAccout(@Value("${lumine.ltuid}") String ltuid, @Value("${lumine.ltoken}") String ltoken) {
        return new LtuidLtoken(ltuid, ltoken);

    }

    @Bean("fakeAccount")
    LtuidLtoken fakeAccount() {
        return new LtuidLtoken("111", "zzz");
    }
}
