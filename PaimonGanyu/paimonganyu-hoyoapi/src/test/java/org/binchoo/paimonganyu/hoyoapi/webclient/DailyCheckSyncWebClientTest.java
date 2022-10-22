package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.assertj.core.util.Arrays;
import org.binchoo.paimonganyu.hoyoapi.error.Retcode;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckMonthlyReport;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.testconfig.TestAccountConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {TestAccountConfig.class})
class DailyCheckSyncWebClientTest {

    DailyCheckSyncWebClient client = new DailyCheckSyncWebClient();

    @Autowired
    @Qualifier("validHoyopass")
    LtuidLtoken validHoyopass;

    @Autowired
    @Qualifier("fakeHoyopass")
    LtuidLtoken fakeHoyopass;

    @Test
    void givenValidAccount_claimDailyCheck_succeedsOrContainsNull() {
        HoyoResponse<DailyCheckResult> response = client.claimDailyCheck(validHoyopass);

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
        HoyoResponse<DailyCheckResult> response = client.claimDailyCheck(fakeHoyopass);

        assertResponseWillEncounterException(response.getRetcode(), NotLoggedInError.class);
    }

    @Test
    void givenValidAccount_getDailyCheckStatus_succeeds() {
        DailyCheckMonthlyReport report = client.getDailyCheckStatus(validHoyopass).getData();

        assertThat(report.getToday()).isEqualTo(
                LocalDateTime.now().minus(4, ChronoUnit.HOURS).toLocalDate());
        assertThat(report.getTotalSignDay()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void givenFakeAccount_getDailyCheckStatus_fails() {
        HoyoResponse<DailyCheckMonthlyReport> response = client.getDailyCheckStatus(fakeHoyopass);

        assertResponseWillEncounterException(response.getRetcode(), NotLoggedInError.class);
    }

    private void assertResponseWillEncounterException(int retcode, Class<? extends RetcodeException> retcodeException) {
        assertThat(retcode).isIn(
                Arrays.asList(retcodeException.getAnnotation(Retcode.class).codes()));
    }
}
