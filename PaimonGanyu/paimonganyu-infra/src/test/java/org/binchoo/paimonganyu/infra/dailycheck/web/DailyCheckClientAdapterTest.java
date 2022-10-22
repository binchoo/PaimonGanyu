package org.binchoo.paimonganyu.infra.dailycheck.web;

import org.binchoo.paimonganyu.dailycheck.DailyCheckRequestResult;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckSyncApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyCheckClientAdapterTest {

    @Mock
    DailyCheckSyncApi dailyCheckSyncApi;

    @InjectMocks
    DailyCheckClientAdapter dailyCheckClientAdapter;

    @Test
    void whenSignInError_sendRequest_setsResultDuplicated() {
        when(dailyCheckSyncApi.claimDailyCheck(any())).thenThrow(SignInException.class);

        DailyCheckRequestResult result = dailyCheckClientAdapter.sendRequest("aa", "aaaa");
        assertThat(result.isDuplicated()).isTrue();
        assertThat(result.hasFailed()).isFalse();
        assertThat(result.getError()).isNull();
        System.out.println(result.getMessage());
    }

    @Test
    void whenUnknownError_sendRequest_setsResultHasFailed() {
        when(dailyCheckSyncApi.claimDailyCheck(any())).thenThrow(RuntimeException.class);

        DailyCheckRequestResult result = dailyCheckClientAdapter.sendRequest("aa", "aaaa");
        assertThat(result.hasFailed()).isTrue();
        assertThat(result.isDuplicated()).isFalse();
        assertThat(result.getError()).isNotNull();
        System.out.println(result.getMessage());
    }

    @Test
    void givenNoError_sendRequest_setsResultCompleted() {
        String responseMessage = "foobar";
        HoyoResponse<DailyCheckResult> goodHoyoResponse = Mockito.mock(HoyoResponse.class);
        when(goodHoyoResponse.getMessage()).thenReturn(responseMessage);
        when(dailyCheckSyncApi.claimDailyCheck(any())).thenReturn(goodHoyoResponse);

        DailyCheckRequestResult result = dailyCheckClientAdapter.sendRequest("aa", "aaaa");
        assertThat(result.hasFailed()).isFalse();
        assertThat(result.isDuplicated()).isFalse();
        assertThat(result.getError()).isNull();
        assertThat(result.getMessage()).isEqualTo(responseMessage);
        System.out.println(result.getMessage());
    }
}