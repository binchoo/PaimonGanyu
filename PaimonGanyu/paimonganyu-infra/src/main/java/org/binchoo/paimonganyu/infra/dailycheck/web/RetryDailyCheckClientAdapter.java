package org.binchoo.paimonganyu.infra.dailycheck.web;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.dailycheck.DailyCheckRequestResult;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckAsyncApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
public class RetryDailyCheckClientAdapter implements DailyCheckClientPort {

    private final DailyCheckAsyncApi dailyCheckApi;

    @Override
    public DailyCheckRequestResult sendRequest(String ltuid, String ltoken) {
        DailyCheckRequestResult requestResult = new DailyCheckRequestResult();
        return dailyCheckApi.claimDailyCheck(new LtuidLtoken(ltuid, ltoken))
                .flatMap(response-> {
                    return handleSuccess(requestResult, response);
                })
                .onErrorResume(e-> handleError(requestResult, e))
                .block();
    }

    private Mono<DailyCheckRequestResult> handleSuccess(DailyCheckRequestResult requestResult, HoyoResponse<DailyCheckResult> response) {
        requestResult.setMessage(response.getMessage());
        return Mono.just(requestResult);
    }

    private Mono<DailyCheckRequestResult> handleError(DailyCheckRequestResult requestResult, Throwable e) {
        Throwable cause = e.getCause();
        if (cause instanceof SignInException)
            requestResult.setDuplicated(true);
        else
            requestResult.setError(Objects.requireNonNullElse(cause, e));
        return Mono.just(requestResult);
    }
}
