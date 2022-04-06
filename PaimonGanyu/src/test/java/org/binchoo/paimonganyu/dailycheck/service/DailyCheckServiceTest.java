package org.binchoo.paimonganyu.dailycheck.service;

import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheckStatus;
import org.binchoo.paimonganyu.dailycheck.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.hoyoapi.HoyolabDailyCheckApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.testconfig.TestAccountConfig;
import org.binchoo.paimonganyu.testconfig.dailycheck.DailyCheckIntegrationConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {DailyCheckIntegrationConfig.class, TestAccountConfig.class})
class DailyCheckServiceTest {

    @Autowired
    @Qualifier("validHoyopass")
    LtuidLtoken validHoyopass;

    @Autowired
    HoyolabDailyCheckApi api;

    UserDailyCheckDynamoRepository repository = Mockito.mock(UserDailyCheckDynamoRepository.class);
    DailyCheckService dailyCheckService = new DailyCheckService(api, repository);

    @Test
    void whenUserHasCheckedInToday_hasCheckedInToday_returnsTrue() {
        final String botUserId = "1";
        final String ltuid = validHoyopass.getLtuid();
        final UserDailyCheck completedUserDailyCheck = UserDailyCheck.builder()
                .botUserId(botUserId).ltuid(ltuid)
                .status(UserDailyCheckStatus.COMPLETED).build();

        when(repository.findByBotUserIdLtuid(any()))
                .thenReturn(Collections.singletonList(completedUserDailyCheck));

        boolean hasCheckedIn = dailyCheckService.hasCheckedInToday(botUserId, ltuid);
        assertThat(hasCheckedIn).isTrue();
    }

    @Test
    void whenUserHasCheckedInToday_hasCheckedIn_onTomorrow_returnsFalse() {
        final String botUserId = "1";
        final String ltuid = validHoyopass.getLtuid();
        final UserDailyCheck completedUserDailyCheck = UserDailyCheck.builder()
                .botUserId(botUserId).ltuid(ltuid)
                .status(UserDailyCheckStatus.COMPLETED).build();

        when(repository.findByBotUserIdLtuid(any()))
                .thenReturn(Collections.singletonList(completedUserDailyCheck));

        boolean hasCheckedInTomorrow = dailyCheckService.hasCheckedIn(botUserId, ltuid, LocalDate.now().plusDays(1));
        assertThat(hasCheckedInTomorrow).isFalse();
    }

    @Test
    void withValidHoyopass_claimDailyCheckIn_throwsNoError() {
        when(repository.save(any()))
                .thenReturn(UserDailyCheck.queued("anyid", validHoyopass.getLtuid()));
        dailyCheckService.claimDailyCheckIn("anyid", validHoyopass.getLtuid(), validHoyopass.getLtoken());
    }
}