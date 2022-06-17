package org.binchoo.paimonganyu.service.dailycheck;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityZeroException;
import org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyCheckServiceTest {

    @InjectMocks
    DailyCheckService dailyCheckService;

    @Mock
    UserDailyCheckCrudPort userDailyCheckCrudPort;

    @Mock
    DailyCheckClientPort dailyCheckClientPort;

    @Mock
    UserDailyCheck mockUserDailyCheck;

    @Test
    void whenUserDailyCheckDone_hasCheckedIn_returnsTrue() {
        String random = RandomString.make();
        LocalDate someDay = getRandomDate();

        when(mockUserDailyCheck.isDoneOn(someDay)).thenReturn(true);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(mockUserDailyCheck));

        boolean hasCheckedInToday = dailyCheckService.hasCheckedIn(random, random, someDay);
        assertThat(hasCheckedInToday).isTrue();
    }

    private LocalDate getRandomDate() {
        Random r = new Random();
        return LocalDate.of(2022 + abs(r.nextInt()) % 10,
                1 + abs(r.nextInt()) % 12,
                1 + abs(r.nextInt()) % 29);
    }

    @Test
    void whenUserDailyCheckNotDone_hasCheckedIn_returnsTrue() {
        String random = RandomString.make();
        LocalDate someDay = getRandomDate();

        when(mockUserDailyCheck.isDoneOn(someDay)).thenReturn(false);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(mockUserDailyCheck));

        boolean hasCheckedInToday = dailyCheckService.hasCheckedIn(random, random, someDay);
        assertThat(hasCheckedInToday).isFalse();
    }

    @Test
    void whenUserDailyCheckDoneOnToday_hasCheckedInToday_returnsTrue() {
        String random = RandomString.make();
        LocalDate today = LocalDate.now();

        when(mockUserDailyCheck.isDoneOn(today)).thenReturn(true);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(mockUserDailyCheck));

        boolean hasCheckedInToday = dailyCheckService.hasCheckedInToday(random, random);
        assertThat(hasCheckedInToday).isTrue();
    }

    @Test
    void whenUserDailyCheckNotDoneOnToday_hasCheckedInToday_returnsTrue() {
        String random = RandomString.make();
        LocalDate today = LocalDate.now();

        when(mockUserDailyCheck.isDoneOn(today)).thenReturn(false);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(mockUserDailyCheck));

        boolean hasCheckedInToday = dailyCheckService.hasCheckedInToday(random, random);
        assertThat(hasCheckedInToday).isFalse();
    }

    @Test
    void whenUserDailyCheckIsEmpty_historyOfUser_throwsException() {
        UserHoyopass user = HoyopassMockUtils.getMockUserHoyopass();
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(QuantityZeroException.class,
                ()-> dailyCheckService.historyOfUser(user, 4)) ;
    }

    @Test
    void getDailyCheckClientPort() {
        assertThat(dailyCheckService.getDailyCheckClient()).isEqualTo(dailyCheckClientPort);
    }

    @Test
    void getRepository() {
        assertThat(dailyCheckService.getRepository()).isEqualTo(userDailyCheckCrudPort);
    }
}