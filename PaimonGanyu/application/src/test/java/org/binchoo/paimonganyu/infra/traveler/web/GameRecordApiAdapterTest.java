package org.binchoo.paimonganyu.infra.traveler.web;

import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.traveler.driven.GameRecordClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@SpringJUnitConfig(classes = {HoyoApiWebClientConfigurer.class})
class GameRecordApiAdapterTest {

    @Autowired
    HoyolabGameRecordApi gameRecordApi;

    GameRecordApiAdapter apiAdapter;

    @BeforeEach
    void init() {
        apiAdapter = new GameRecordApiAdapter(gameRecordApi);
    }

    @Test
    void getStatusOf() {

    }
}