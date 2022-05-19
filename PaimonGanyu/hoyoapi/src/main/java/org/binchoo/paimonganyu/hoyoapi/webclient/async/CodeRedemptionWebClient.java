package org.binchoo.paimonganyu.hoyoapi.webclient.async;

import org.binchoo.paimonganyu.hoyoapi.HoyoCodeRedemptionApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.AccountIdCookieToken;
import org.binchoo.paimonganyu.hoyoapi.pojo.CodeRedemptionResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.binchoo.paimonganyu.hoyoapi.HoyolabConstant.*;

/**
 * @author : jbinchoo
 * @since : 2022-04-22
 */
@Component
public class CodeRedemptionWebClient implements HoyoCodeRedemptionApi, Retriable {

    public static final int DEFAULT_RETRY_ATTEMPTS = 3;
    public static final int DEFAULT_RETRY_DELAY_MILLIS = 5001;

    private final WebClient webClient;

    private Retry retryObject;

    public CodeRedemptionWebClient() {
        this(DEFAULT_RETRY_ATTEMPTS, DEFAULT_RETRY_DELAY_MILLIS);
    }

    public CodeRedemptionWebClient(int retryAttempts, int retryDelaysMillis) {
        this.webClient = WebClient.create(getBaseUrl());
        this.retryObject = Retry.fixedDelay(retryAttempts, Duration.ofMillis(retryDelaysMillis));
    }

    @Override
    public Mono<HoyoResponse<CodeRedemptionResult>> redeem(AccountIdCookieToken accountIdCookieToken, String uid, String server, String code) {
       return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam(PARAM_CDKEY, code)
                    .queryParam(PARAM_UID, uid)
                    .queryParam(PARAM_GAME_BIZ, "hk4e_global")
                    .queryParam(PARAM_LANG, "en")
                    .queryParam(PARAM_REGION, server)
                    .build())
               .cookie(COOKIE_ACCOUNT_ID, accountIdCookieToken.getAccountId())
               .cookie(COOKIE_COOKIE_TOKEN, accountIdCookieToken.getCookieToken())
               .retrieve()
               .bodyToMono(new ParameterizedTypeReference<HoyoResponse<CodeRedemptionResult>>() {});
    }

    @Override
    public void setRetry(Retry retry) {
        this.retryObject = retry;
    }

    @Override
    public Retry getRetryObject() {
        return retryObject;
    }
}
