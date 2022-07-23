package org.binchoo.paimonganyu.dailycheck;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDailyCheckTest {

    @Mock
    DailyCheckClientPort dailyCheckClientPort;

    @Test
    void whenRequestFailed_userDailyCheck_hasFailedStatus() {
        String random = RandomString.make();
        UserDailyCheck userDailyCheck = UserDailyCheck.of(random, random, random);

        when(dailyCheckClientPort.sendRequest(any(), any())).thenReturn(whenRequestFailed());

        UserDailyCheck updated = userDailyCheck.doRequest(dailyCheckClientPort);
        checkUserDailyCheckStatus(random, updated, UserDailyCheckStatus.FAILED);
    }

    @Test
    void whenRequestDuplicated_userDailyCheck_hasDuplicatedStatus() {
        String random = RandomString.make();
        UserDailyCheck userDailyCheck = UserDailyCheck.of(random, random, random);

        when(dailyCheckClientPort.sendRequest(any(), any())).thenReturn(whenRequestDuplicated());

        UserDailyCheck updated = userDailyCheck.doRequest(dailyCheckClientPort);
        checkUserDailyCheckStatus(random, updated, UserDailyCheckStatus.DUPLICATE);
    }

    @Test
    void whenRequestCompleted_userDailyCheck_isDoneOnToday() {
        String random = RandomString.make();
        UserDailyCheck userDailyCheck = UserDailyCheck.of(random, random, random);

        when(dailyCheckClientPort.sendRequest(any(), any())).thenReturn(whenRequestCompleted());

        UserDailyCheck updated = userDailyCheck.doRequest(dailyCheckClientPort);
        assertThat(userDailyCheck.isDoneOn(LocalDate.now())).isFalse();
        assertThat(updated.isDoneOn(LocalDate.now())).isTrue();
    }

    @Test
    void whenRequestDuplicated_userDailyCheck_isDoneOnToday() {
        String random = RandomString.make();
        UserDailyCheck userDailyCheck = UserDailyCheck.of(random, random, random);


        when(dailyCheckClientPort.sendRequest(any(), any())).thenReturn(whenRequestDuplicated());

        UserDailyCheck updated = userDailyCheck.doRequest(dailyCheckClientPort);
        assertThat(userDailyCheck.isDoneOn(LocalDate.now())).isFalse();
        assertThat(updated.isDoneOn(LocalDate.now())).isTrue();
    }

    @Test
    void whenRequestCompleted_userDailyCheck_hasCompletedStatus() {
        String random = RandomString.make();
        UserDailyCheck userDailyCheck = UserDailyCheck.of(random, random, random);

        when(dailyCheckClientPort.sendRequest(any(), any())).thenReturn(whenRequestCompleted());

        UserDailyCheck updated = userDailyCheck.doRequest(dailyCheckClientPort);
        checkUserDailyCheckStatus(random, updated, UserDailyCheckStatus.COMPLETED);
    }

    private DailyCheckRequestResult whenRequestFailed() {
        DailyCheckRequestResult result = new DailyCheckRequestResult();
        result.setError(new Exception());
        return result;
    }

    private DailyCheckRequestResult whenRequestDuplicated() {
        DailyCheckRequestResult result = new DailyCheckRequestResult();
        result.setDuplicated(true);
        return result;
    }

    private DailyCheckRequestResult whenRequestCompleted() {
        DailyCheckRequestResult result = new DailyCheckRequestResult();
        result.setMessage("foobar");
        return result;
    }

    private void checkUserDailyCheckStatus(String random, UserDailyCheck updated, UserDailyCheckStatus status) {
        assertThat(updated.getStatus()).isEqualTo(status);
        assertThat(updated.getBotUserId()).isEqualTo(random);
        assertThat(updated.getLtuid()).isEqualTo(random);
        assertThat(updated.getLtoken()).isEqualTo(random);
    }
}