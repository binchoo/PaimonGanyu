package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.assertj.core.util.Arrays;
import org.binchoo.paimonganyu.config.TestAccountConfig;
import org.binchoo.paimonganyu.hoyoapi.error.Retcode;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckMonthlyReport;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {TestAccountConfig.class})
class HoyolabDailyCheckWebClientTest {

    HoyolabDailyCheckWebClient hoyolabDailyCheckApi = new HoyolabDailyCheckWebClient();

    @Autowired
    @Qualifier("validHoyopass")
    LtuidLtoken validHoyopass;

    @Autowired
    @Qualifier("fakeHoyopass")
    LtuidLtoken fakeHoyopass;

    @Test
    void givenValidAccount_claimDailyCheck_succeedsOrContainsNull() {
        HoyoResponse<DailyCheckResult> response = hoyolabDailyCheckApi.claimDailyCheck(validHoyopass);

        int retcode = response.getRetcode();
        if (response.getData() == null) {
            assertResponseWillEncounterException(retcode, SignInException.class);
        } else {
            assertThat(retcode).isEqualTo(0);
            System.out.println(response.getData());
        }
        System.out.println(response.getMessage());
    }

    @Test
    void givenFakeAccount_claimDailyCheck_fails() {
        HoyoResponse<DailyCheckResult> response = hoyolabDailyCheckApi.claimDailyCheck(fakeHoyopass);

        assertResponseWillEncounterException(response.getRetcode(), NotLoggedInError.class);
    }

    @Test
    void givenValidAccount_getDailyCheckStatus_succeeds() {
        DailyCheckMonthlyReport report = hoyolabDailyCheckApi.getDailyCheckStatus(validHoyopass).getData();

        System.out.println(report);
        assertThat(report.getToday()).isEqualTo(LocalDate.now());
        assertThat(report.getTotalSignDay()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void givenFakeAccount_getDailyCheckStatus_fails() {
        HoyoResponse<DailyCheckMonthlyReport> response = hoyolabDailyCheckApi.getDailyCheckStatus(fakeHoyopass);

        assertResponseWillEncounterException(response.getRetcode(), NotLoggedInError.class);
    }

    private void assertResponseWillEncounterException(int retcode, Class<? extends RetcodeException> retcodeException) {
        assertThat(retcode).isIn(
                Arrays.asList(retcodeException.getAnnotation(Retcode.class).codes()));
    }
}