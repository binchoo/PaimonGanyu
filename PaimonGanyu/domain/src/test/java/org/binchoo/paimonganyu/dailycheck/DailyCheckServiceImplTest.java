package org.binchoo.paimonganyu.dailycheck;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.service.DailyCheckServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyCheckServiceImplTest {

    @InjectMocks
    DailyCheckServiceImpl dailyCheckServiceImpl;

    @Mock
    UserDailyCheckCrudPort userDailyCheckCrudPort;

    @Mock
    DailyCheckClientPort dailyCheckClientPort;

    @Mock
    UserDailyCheck userDailyCheck;

    @Test
    void afterSendingRequest_claimDailyCheckIn_savesUpdatedUserDailyCheckToRepository() {
        String random = RandomString.make();
        UserDailyCheck updatedUserDailyCheck = UserDailyCheck.builder()
                .botUserId(random).ltoken(random).ltuid(random)
                .status(UserDailyCheckStatus.COMPLETED).build();

        when(userDailyCheckCrudPort.save(any())).thenReturn(userDailyCheck);
        when(userDailyCheck.doRequest(dailyCheckClientPort)).thenReturn(updatedUserDailyCheck);

        dailyCheckServiceImpl.claimDailyCheckIn(random, random, random);
        verify(userDailyCheckCrudPort).save(updatedUserDailyCheck);
    }

    @Test
    void whenUserDailyCheckDone_hasCheckedIn_returnsTrue() {
        String random = RandomString.make();
        LocalDate someDay = getRandomDate();

        when(userDailyCheck.isDoneOn(someDay)).thenReturn(true);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(userDailyCheck));

        boolean hasCheckedInToday = dailyCheckServiceImpl.hasCheckedIn(random, random, someDay);
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

        when(userDailyCheck.isDoneOn(someDay)).thenReturn(false);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(userDailyCheck));

        boolean hasCheckedInToday = dailyCheckServiceImpl.hasCheckedIn(random, random, someDay);
        assertThat(hasCheckedInToday).isFalse();
    }

    @Test
    void whenUserDailyCheckDoneOnToday_hasCheckedInToday_returnsTrue() {
        String random = RandomString.make();
        LocalDate today = LocalDate.now();

        when(userDailyCheck.isDoneOn(today)).thenReturn(true);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(userDailyCheck));

        boolean hasCheckedInToday = dailyCheckServiceImpl.hasCheckedInToday(random, random);
        assertThat(hasCheckedInToday).isTrue();
    }

    @Test
    void whenUserDailyCheckNotDoneOnToday_hasCheckedInToday_returnsTrue() {
        String random = RandomString.make();
        LocalDate today = LocalDate.now();

        when(userDailyCheck.isDoneOn(today)).thenReturn(false);
        when(userDailyCheckCrudPort.findByBotUserIdLtuid(random, random)).thenReturn(
                Collections.singletonList(userDailyCheck));

        boolean hasCheckedInToday = dailyCheckServiceImpl.hasCheckedInToday(random, random);
        assertThat(hasCheckedInToday).isFalse();
    }

    @Test
    void getDailyCheckClientPort() {
        assertThat(dailyCheckServiceImpl.getDailyCheckClientPort()).isEqualTo(dailyCheckClientPort);
    }

    @Test
    void getRepository() {
        assertThat(dailyCheckServiceImpl.getRepository()).isEqualTo(userDailyCheckCrudPort);
    }
}