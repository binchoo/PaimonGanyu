package org.binchoo.paimonganyu.service.dailycheck;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.exception.NoUserDailyCheckException;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyCheckServiceTest {

    @InjectMocks
    DailyCheckService service;

    @Mock
    UserDailyCheck mockPojo;

    @Mock
    UserDailyCheckCrudPort mockCrud;

    @Mock
    DailyCheckClientPort mockClient;

    @Test
    void whenUserDailyCheckDone_hasCheckedIn_returnsTrue() {
        String someStr = randomString();
        LocalDate someDay = randomDate();
        when(mockPojo.isDoneOn(someDay)).thenReturn(true);
        when(mockCrud.findByBotUserIdLtuid(someStr, someStr)).thenReturn(List.of(mockPojo));

        boolean hasCheckedInToday = service.hasCheckedIn(someStr, someStr, someDay);

        assertThat(hasCheckedInToday).isTrue();
    }

    @Test
    void whenUserDailyCheckNotDone_hasCheckedIn_returnsTrue() {
        String random = randomString();
        LocalDate someDay = randomDate();
        when(mockPojo.isDoneOn(someDay)).thenReturn(false);
        when(mockCrud.findByBotUserIdLtuid(random, random)).thenReturn(List.of(mockPojo));

        boolean hasCheckedInToday = service.hasCheckedIn(random, random, someDay);

        assertThat(hasCheckedInToday).isFalse();
    }

    @Test
    void whenUserDailyCheckDoneOnToday_hasCheckedInToday_returnsTrue() {
        String random = randomString();
        LocalDate today = LocalDate.now();
        when(mockPojo.isDoneOn(today)).thenReturn(true);
        when(mockCrud.findByBotUserIdLtuid(random, random)).thenReturn(List.of(mockPojo));

        boolean hasCheckedInToday = service.hasCheckedInToday(random, random);

        assertThat(hasCheckedInToday).isTrue();
    }

    @Test
    void whenUserDailyCheckNotDoneOnToday_hasCheckedInToday_returnsTrue() {
        String random = randomString();
        LocalDate today = LocalDate.now();
        when(mockPojo.isDoneOn(today)).thenReturn(false);
        when(mockCrud.findByBotUserIdLtuid(random, random)).thenReturn(List.of(mockPojo));

        boolean hasCheckedInToday = service.hasCheckedInToday(random, random);

        assertThat(hasCheckedInToday).isFalse();
    }

    @Test
    void whenUserDailyCheckIsEmpty_historyOfUser_throwsException() {
        UserHoyopass user = HoyopassMockUtils.mockUserHoyopass();
        when(mockCrud.findByBotUserIdLtuid(any(), any()))
                .thenReturn(List.of());

        assertThrows(NoUserDailyCheckException.class,
                ()-> service.historyOfUser(user, 4)) ;
    }

    @Test
    void getDailyCheckClientPort() {
        assertThat(service.getDailyCheckClient()).isEqualTo(mockClient);
    }

    @Test
    void getRepository() {
        assertThat(service.getDailyCheckCrud()).isEqualTo(mockCrud);
    }

    private String randomString() {
        return RandomString.make();
    }

    private LocalDate randomDate() {
        Random r = new Random();
        return LocalDate.of(2022 + abs(r.nextInt()) % 10,
                1 + abs(r.nextInt()) % 12,
                1 + abs(r.nextInt()) % 29);
    }
}