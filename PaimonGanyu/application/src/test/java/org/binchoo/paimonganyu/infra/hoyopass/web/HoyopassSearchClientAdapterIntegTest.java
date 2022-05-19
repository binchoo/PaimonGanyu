package org.binchoo.paimonganyu.infra.hoyopass.web;

import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.testconfig.TestHoyopassCredentialsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {HoyoApiWebClientConfigurer.class, TestHoyopassCredentialsConfig.class})
class HoyopassSearchClientAdapterIntegTest {

    @Autowired
    HoyolabAccountApi accountApi;

    @Autowired
    HoyolabGameRecordApi gameRecordApi;

    @Autowired
    @Qualifier("valid0")
    TestHoyopassCredentialsConfig.TestCredentials valid0;

    @Autowired
    TestHoyopassCredentialsConfig.InvalidTestCredentials invalid0;

    HoyopassSearchClientAdapter hoyopassSearchClientAdapter;

    @BeforeEach
    public void setup() {
        assert accountApi != null;
        assert gameRecordApi != null;
        hoyopassSearchClientAdapter = new HoyopassSearchClientAdapter(accountApi, gameRecordApi);
    }

    @Test
    void givenValidHoyopass_findUids_returnsMatchingUids() {
        Hoyopass validHoyopass = givenHoyopassOf(valid0);
        List<UserGameRole> realUserGameRoles = accountApi.getUserGameRoles(valid0)
                .getData().getList();

        var uids = hoyopassSearchClientAdapter.findUids(validHoyopass);

        assertThat(uids).hasSameSizeAs(realUserGameRoles);
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
        Hoyopass invalidHoyopass = givenHoyopassOf(invalid0);

        assertThrows(IllegalArgumentException.class, ()->
                hoyopassSearchClientAdapter.findUids(invalidHoyopass));
    }

    private Hoyopass givenHoyopassOf(LtuidLtoken ltuidLtoken) {
        return Hoyopass.builder()
                .credentials(HoyopassCredentials.builder()
                        .ltuid(ltuidLtoken.getLtuid())
                        .ltoken(ltuidLtoken.getLtoken())
                        .build())
                .build();
    }
}
