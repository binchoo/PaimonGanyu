package org.binchoo.paimonganyu.hoyoapi.webclient.async;

import org.binchoo.paimonganyu.hoyoapi.HoyolabConstant;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckAsyncApi;
import org.binchoo.paimonganyu.hoyoapi.Retriable;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckMonthlyReport;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.binchoo.paimonganyu.hoyoapi.HoyolabConstant.COOKIE_LTOKEN;
import static org.binchoo.paimonganyu.hoyoapi.HoyolabConstant.COOKIE_LTUID;

/**
 * @author : jbinchoo
 * @since : 2022-10-22
 */
@Component
public class DailyCheckAsyncWebClient implements DailyCheckAsyncApi, Retriable {

    public static final int DEFAULT_RETRY_ATTEMPTS = 3;
    public static final int DEFAULT_RETRY_DELAY_MILLIS = 5001;

    private static final String CLAIM_URL = "/sign";
    private static final String MONTHLY_REPORT_URL = "/info";

    private WebClient webClient;
    private Retry retryObject;

    public DailyCheckAsyncWebClient() {
        this(DEFAULT_RETRY_ATTEMPTS, DEFAULT_RETRY_DELAY_MILLIS);
    }

    public DailyCheckAsyncWebClient(int retryAttempts, int retryDelaysMillis) {
        this.webClient = WebClient.create(getBaseUrl());
        this.retryObject = Retry.fixedDelay(retryAttempts, Duration.ofMillis(retryDelaysMillis));
    }

    /**
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException
     */
    @Override
    public Mono<HoyoResponse<DailyCheckResult>> claimDailyCheck(LtuidLtoken ltuidLtoken) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(CLAIM_URL)
                        .queryParam(HoyolabConstant.PARAM_ACT_ID, HoyolabConstant.ACT_ID_DAILYCHECK)
                        .build())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<HoyoResponse<DailyCheckMonthlyReport>> getDailyCheckStatus(LtuidLtoken ltuidLtoken) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MONTHLY_REPORT_URL)
                        .queryParam(HoyolabConstant.PARAM_ACT_ID, HoyolabConstant.ACT_ID_DAILYCHECK)
                        .build())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    @Override
    public void setRetry(Retry retry) {
        this.retryObject = retry;
    }

    @Override
    public Retry getRetry() {
        return this.retryObject;
    }
}
