package org.binchoo.paimonganyu.infra.traveler.web;

import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyopass.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@Import(GameRecordApiAdapterTest.TestUserConfig.class)
@SpringJUnitConfig(classes = {HoyoApiWebClientConfigurer.class})
class GameRecordApiAdapterTest {

    GameRecordApiAdapter apiAdapter;

    @Autowired
    HoyolabGameRecordApi gameRecordApi;

    @Autowired
    HoyolabAccountApi accountApi;

    @Autowired
    UserHoyopass user;


    @BeforeEach
    void init() {
        apiAdapter = new GameRecordApiAdapter(gameRecordApi);
    }

    @Test
    void getStatusOf() {
        var result = apiAdapter.getStatusOf(user, null);
        System.out.println(result);
    }

    @TestConfiguration
    @PropertySource("classpath:accounts.properties")
    public static class TestUserConfig {

        @Bean
        UserHoyopass user(@Value("${aether.ltuid}") String ltuid,
                          @Value("${aether.ltoken}") String ltoken,
                          @Value("${aether.region}") String region,
                          @Value("${aether.uid}") String uid) {

            return UserHoyopass.builder()
                    .botUserId("foo")
                    .hoyopasses(List.of(Hoyopass.builder()
                            .credentials(HoyopassCredentials.builder()
                                    .ltuid(ltuid)
                                    .ltoken(ltoken)
                                    .build())
                                    .uids(List.of(Uid.builder()
                                                    .region(Region.fromString(region))
                                                    .characterLevel(10)
                                                    .isLumine(false)
                                                    .characterName("footraveler")
                                                    .uidString(uid)
                                            .build()))
                            .build()))
                    .build();
        }
    }
}