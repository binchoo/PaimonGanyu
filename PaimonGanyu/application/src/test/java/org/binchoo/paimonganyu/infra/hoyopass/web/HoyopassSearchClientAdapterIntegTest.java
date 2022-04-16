package org.binchoo.paimonganyu.infra.hoyopass.web;

import org.binchoo.paimonganyu.chatbot.config.HoyoApiConfig;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {HoyoApiConfig.class, HoyopassSearchClientAdapterIntegTest.class})
@TestPropertySource("classpath:accounts.properties")
class HoyopassSearchClientAdapterIntegTest {

    @Autowired
    HoyolabAccountApi accountApi;

    @Autowired
    HoyolabGameRecordApi gameRecordApi;

    HoyopassSearchClientAdapter hoyopassSearchClientAdapter;

    @Autowired
    ValidLtuidLtoken validLtuidLtoken;

    @Autowired
    InvalidLtuidLtoken invalidLtuidLtoken;

    @BeforeEach
    public void setup() {
        assert accountApi != null;
        assert gameRecordApi != null;
        hoyopassSearchClientAdapter = new HoyopassSearchClientAdapter(accountApi, gameRecordApi);
    }

    @Test
    void givenValidHoyopass_findUids_returnsMatchingUids() {
        Hoyopass validHoyopass = Hoyopass.builder()
                .ltuid(validLtuidLtoken.getLtuid())
                .ltoken(validLtuidLtoken.getLtoken())
                .build();

        List<UserGameRole> realUserGameRoles = accountApi.getUserGameRoles(validLtuidLtoken)
                .getData().getList();

        var uids = hoyopassSearchClientAdapter.findUids(validHoyopass);

        assertThat(uids.size()).isEqualTo(realUserGameRoles.size());
        IntStream.range(0, uids.size()).forEach (i-> {
            var uid = uids.get(i);
            var ugr = realUserGameRoles.get(i);
            checkEquity(uid, ugr);
        });
    }

    private void checkEquity(Uid uid, UserGameRole ugr) {
        assertThat(uid.getRegion()).isEqualTo(Region.fromString(ugr.getRegion()));
        assertThat(uid.getCharacterLevel()).isEqualTo(ugr.getLevel());
        assertThat(uid.getCharacterName()).isEqualTo(ugr.getNickname());
        assertThat(uid.getUidString()).isEqualTo(ugr.getGameUid());
    }

    @Test
    void givenInvalidHoyopass_findUids_throwsError() {
        Hoyopass validHoyopass = Hoyopass.builder()
                .ltuid(invalidLtuidLtoken.getLtuid())
                .ltoken(invalidLtuidLtoken.getLtoken())
                .build();

        assertThrows(IllegalArgumentException.class, ()->
                hoyopassSearchClientAdapter.findUids(validHoyopass));
    }

    private static class ValidLtuidLtoken extends LtuidLtoken {

        public ValidLtuidLtoken(String ltuid, String ltoken) {
            super(ltuid, ltoken);
        }
    }

    private static class InvalidLtuidLtoken extends LtuidLtoken {

        public InvalidLtuidLtoken(String ltuid, String ltoken) {
            super(ltuid, ltoken);
        }
    }

    @Bean
    public ValidLtuidLtoken validHoyopassData(@Value("${valid.ltuid}") String ltuid,
                                              @Value("${valid.ltoken}") String ltoken){
        return new ValidLtuidLtoken(ltuid, ltoken);
    }

    @Bean
    public InvalidLtuidLtoken invalidLtuidLtoken(){
        return new InvalidLtuidLtoken("foo", "bar");
    }
}
