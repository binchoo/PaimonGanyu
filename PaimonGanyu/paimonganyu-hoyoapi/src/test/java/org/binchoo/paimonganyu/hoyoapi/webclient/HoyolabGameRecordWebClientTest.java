package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.binchoo.paimonganyu.hoyoapi.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatars;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.enums.DataSwitch;
import org.binchoo.paimonganyu.hoyoapi.support.DsHeaderGenerator;
import org.binchoo.paimonganyu.hoyoapi.testconfig.TestAccountConfig;
import org.binchoo.paimonganyu.hoyoapi.testconfig.TestAccountDetails;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {TestAccountConfig.class})
class HoyolabGameRecordWebClientTest {

    HoyolabGameRecordWebClient gameRecordApi = new HoyolabGameRecordWebClient(DsHeaderGenerator.getDefault());

    @Autowired
    @Qualifier("aetherAccountDetails")
    TestAccountDetails aetherAccount;

    @Autowired
    @Qualifier("lumineAccountDetails")
    TestAccountDetails lumineAccount; // I do not breed this account.

    final long aetherId = 10000005;

    @Test
    void givenAetherAccount_getAllAvartar_successful() {
        HoyoResponse<GenshinAvatars> response = gameRecordApi.getAllAvartars(aetherAccount.getLtuidLtoken(),
                aetherAccount.getUid(), aetherAccount.getRegion());

        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().containsLumine()).isFalse();
    }

    @Test
    void givenLumineAccount_getAllAvartar_successful() {
        HoyoResponse<GenshinAvatars> response = gameRecordApi.getAllAvartars(lumineAccount.getLtuidLtoken(),
                lumineAccount.getUid(), lumineAccount.getRegion());

        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().containsLumine()).isTrue();
    }

    @SuppressWarnings("Deprecated API")
    @Disabled("Including chararcterIds in the request doesn't change its response.")
    @Test
    void whenAetherDesignated_fetchAvartars_retunsOnlyAether() {
        HoyoResponse<GenshinAvatars> response = gameRecordApi.fetchAvartars(lumineAccount.getLtuidLtoken(),
                lumineAccount.getUid(), lumineAccount.getRegion(), aetherId);

        assertThat(response.getData()).isNotNull();

        GenshinAvatars avatars = response.getData();
        assertThat(avatars.containsLumine()).isEqualTo(false);
        assertThat(avatars.getAvatars().size()).isEqualTo(1);
    }

    @Test
    void getDailyNote_successful() {
        HoyoResponse<DailyNote> response = gameRecordApi.getDailyNote(lumineAccount.getLtuidLtoken(),
                lumineAccount.getUid(), lumineAccount.getRegion());

        DailyNote dailyNote = response.getData();
        assertThat(dailyNote).isNotNull();
        assertThat(dailyNote.getCurrentResin()).isEqualTo(160);
        assertThat(dailyNote.getCurrentHomeCoin()).isEqualTo(0);
    }

    @EnumSource(value = DataSwitch.class, names = {"ChronicleOnProfile", "CharacterDetails", "DailyNotes"})
    @ParameterizedTest
    void changeDataSwitch_sucessful(DataSwitch dataSwitch) {
        var turnOff = gameRecordApi.changeDataSwitch(aetherAccount.getLtuidLtoken(),
                dataSwitch, false);
        var turnOn = gameRecordApi.changeDataSwitch(aetherAccount.getLtuidLtoken(),
                dataSwitch, true);

        assertThat(turnOff.getData()).isNotNull();
        assertThat(turnOn.getData()).isNotNull();
    }
}