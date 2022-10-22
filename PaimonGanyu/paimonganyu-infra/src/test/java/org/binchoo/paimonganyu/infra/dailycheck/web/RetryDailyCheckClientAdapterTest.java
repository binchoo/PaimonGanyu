package org.binchoo.paimonganyu.infra.dailycheck.web;

import org.binchoo.paimonganyu.dailycheck.DailyCheckRequestResult;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckAsyncApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetryDailyCheckClientAdapterTest {

    @Mock
    DailyCheckAsyncApi client;

    @InjectMocks
    RetryDailyCheckClientAdapter clientAdapter;

    @Test
    void whenSignInError_sendRequest_setsResultDuplicated() {
        when(client.claimDailyCheck(any()))
                .thenReturn(Mono.error(reactor.core.Exceptions.retryExhausted("foobar", new SignInException())));

        DailyCheckRequestResult result = clientAdapter.sendRequest("aa", "aaaa");
        assertThat(result.isDuplicated()).isTrue();
        assertThat(result.hasFailed()).isFalse();
        assertThat(result.getError()).isNull();
        System.out.println(result.getMessage());
    }

    @Test
    void whenUnknownError_sendRequest_setsResultHasFailed() {
        when(client.claimDailyCheck(any()))
                .thenReturn(Mono.error(new Exception()));

        DailyCheckRequestResult result = clientAdapter.sendRequest("aa", "aaaa");
        assertThat(result.hasFailed()).isTrue();
        assertThat(result.isDuplicated()).isFalse();
        assertThat(result.getError()).isNotNull();
        System.out.println(result.getMessage());
    }

    @Test
    void givenNoError_sendRequest_setsResultCompleted() {
        String responseMessage = "foobar";
        HoyoResponse<DailyCheckResult> goodResponse = Mockito.mock(HoyoResponse.class);
        when(goodResponse.getMessage()).thenReturn(responseMessage);

        Mono<HoyoResponse<DailyCheckResult>> goodMono = Mono.just(goodResponse);
        when(client.claimDailyCheck(any())).thenReturn(goodMono);

        DailyCheckRequestResult result = clientAdapter.sendRequest("aa", "aaaa");
        assertThat(result.hasFailed()).isFalse();
        assertThat(result.isDuplicated()).isFalse();
        assertThat(result.getError()).isNull();
        assertThat(result.getMessage()).isEqualTo(responseMessage);
        System.out.println(result.getMessage());
    }
}