package org.binchoo.paimonganyu.integration.hoyopass.traveler;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.DataSwitchConfigurer;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.*;
import org.binchoo.paimonganyu.infra.traveler.web.GameRecordApiAdapter;
import org.binchoo.paimonganyu.traveler.TravelerStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@Slf4j
@SpringJUnitConfig(classes = {
        HoyoApiWebClientConfigurer.class,
        GameRecordApiAdapterIntegTest.TestUserConfig.class})
class GameRecordApiAdapterIntegTest {

    GameRecordApiAdapter apiAdapter;
    TravelerStatus errorneousStatus;

    @Autowired
    HoyolabGameRecordApi gameRecordApi;

    @Autowired
    DataSwitchConfigurer dataSwitchConfigurer;

    @Autowired
    UserHoyopass user;

    @BeforeEach
    void init() {
        apiAdapter = new GameRecordApiAdapter(gameRecordApi);
        errorneousStatus = TravelerStatus.erroneous();
    }

    @AfterEach
    void recoverSwitches() {
        turnDataSwitchesON(user);
    }

    @Test
    void whenDataSwitchesAreON_getStatusOf_successful() {
        var result = apiAdapter.getStatusOf(user, null);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(it-> !errorneousStatus.equals(it));
        log.debug(result.toString());
    }

    @Test
    void whenDataSwitchesAreOFF_getStatusOf_isEmpty() {
        turnDataSwitchesOFF(user);
        var result = apiAdapter.getStatusOf(user, null);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(errorneousStatus::equals);
        log.debug(result.toString());
    }

    private void turnDataSwitchesOFF(UserHoyopass user) {
        user.listHoyopasses().stream()
                .map(this::toLtuidLtoken)
                .forEach(this::turnDataSwitchesOFF);
    }

    private void turnDataSwitchesOFF(LtuidLtoken ltlt) {
        dataSwitchConfigurer.turnOff(ltlt);
    }

    private void turnDataSwitchesON(UserHoyopass user) {
        user.listHoyopasses().stream()
                .map(this::toLtuidLtoken)
                .forEach(this::turnDataSwitchesON);
    }

    private void turnDataSwitchesON(LtuidLtoken ltlt) {
        dataSwitchConfigurer.turnOn(ltlt);
    }

    private LtuidLtoken toLtuidLtoken(Hoyopass hoyopass) {
        return new LtuidLtoken(hoyopass.getLtuid(), hoyopass.getLtoken());
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