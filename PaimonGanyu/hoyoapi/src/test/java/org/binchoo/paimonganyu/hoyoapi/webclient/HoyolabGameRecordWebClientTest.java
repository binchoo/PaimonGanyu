package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.binchoo.paimonganyu.hoyoapi.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatars;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.support.DsHeaderGenerator;
import org.binchoo.paimonganyu.hoyoapi.testconfig.TestAccountConfig;
import org.binchoo.paimonganyu.hoyoapi.testconfig.TestAccountDetails;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {TestAccountConfig.class})
class HoyolabGameRecordWebClientTest {

    HoyolabGameRecordWebClient gameRecordApi = new HoyolabGameRecordWebClient(DsHeaderGenerator.getDefault());

    @Autowired
    @Qualifier("aetherAccountDetails")
    TestAccountDetails aetherAccountDetails;

    @Autowired
    @Qualifier("lumineAccountDetails")
    TestAccountDetails lumineAccountDetails; // I do not breed this account.

    final long aetherId = 10000005;

    @Test
    void givenAetherAccount_getAllAvartar_successful() {
        HoyoResponse<GenshinAvatars> response = gameRecordApi.getAllAvartars(aetherAccountDetails.getLtuidLtoken(),
                aetherAccountDetails.getUid(), aetherAccountDetails.getRegion());

        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().containsLumine()).isEqualTo(false);
    }

    @Test
    void givenLumineAccount_getAllAvartar_successful() {
        HoyoResponse<GenshinAvatars> response = gameRecordApi.getAllAvartars(lumineAccountDetails.getLtuidLtoken(),
                lumineAccountDetails.getUid(), lumineAccountDetails.getRegion());

        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().containsLumine()).isEqualTo(true);
    }

    @SuppressWarnings("Deprecated API")
    @Disabled("Including chararcterIds in the request doesn't change its response.")
    @Test
    void whenAetherDesignated_fetchAvartars_retunsOnlyAether() {
        HoyoResponse<GenshinAvatars> response = gameRecordApi.fetchAvartars(lumineAccountDetails.getLtuidLtoken(),
                lumineAccountDetails.getUid(), lumineAccountDetails.getRegion(), aetherId);

        assertThat(response.getData()).isNotNull();

        GenshinAvatars avatars = response.getData();
        assertThat(avatars.containsLumine()).isEqualTo(false);
        assertThat(avatars.getAvatars().size()).isEqualTo(1);
    }

    @Test
    void getDailyNote() {
        HoyoResponse<DailyNote> response = gameRecordApi.getDailyNote(lumineAccountDetails.getLtuidLtoken(),
                lumineAccountDetails.getUid(), lumineAccountDetails.getRegion());

        assertThat(response.getData()).isNotNull();

        DailyNote dailyNote = response.getData();
        assertThat(dailyNote.getCurrentResin()).isEqualTo(160);
        assertThat(dailyNote.getCurrentHomeCoin()).isEqualTo(0);
    }
}