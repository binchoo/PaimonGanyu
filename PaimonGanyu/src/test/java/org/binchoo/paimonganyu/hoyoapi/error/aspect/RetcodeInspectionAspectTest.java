package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.binchoo.paimonganyu.PaimonGanyuApp;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {PaimonGanyuApp.class})
class RetcodeInspectionAspectTest {

    @Autowired
    HoyolabAccountApi accountApi;

    @Autowired
    HoyolabGameRecordApi gameRecordApi;

    Hoyopass hoyopass = Hoyopass.builder()
            .ltuid("77407897")
            .ltoken("rN0anFtemzPu8vfYLW0hkLkysJeidRk3vN6YGtjA")
            .build();

    @Test
    void getAllCharacter() {
    }

    @Test
    void getGivenCharacters() {
    }

    @Test
    void getDailyNote() throws ClassNotFoundException {
    }
}