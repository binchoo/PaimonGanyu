package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.binchoo.paimonganyu.hoyoapi.HoyolabDailyCheckApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabConstant;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckMonthlyReport;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.binchoo.paimonganyu.hoyoapi.HoyolabConstant.*;

@Component
public class HoyolabDailyCheckWebClient implements HoyolabDailyCheckApi {

    /**
     * 원신 일일 체크 API - POST API
     */
    private static final String CLAIM_URL = "/sign";

    /**
     * 원신 일일 체크 현황 API - GET API
     */
    private static final String MONTHLY_REPORT_URL = "/info";

    private WebClient webClient;

    public HoyolabDailyCheckWebClient() {
        this.webClient = WebClient.create(getBaseUrl());
    }

    /**
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException
     */
    @Override
    public HoyoResponse<DailyCheckResult> claimDailyCheck(LtuidLtoken ltuidLtoken) {
        ResponseEntity<HoyoResponse<DailyCheckResult>> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                    .path(CLAIM_URL)
                    .queryParam(HoyolabConstant.PARAM_ACT_ID, HoyolabConstant.ACT_ID_DAILYCHECK)
                    .build())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<DailyCheckResult>>() {})
                .block();

        return response.getBody();
    }

    /**
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError
     */
    @Override
    public HoyoResponse<DailyCheckMonthlyReport> getDailyCheckStatus(LtuidLtoken ltuidLtoken) {
        ResponseEntity<HoyoResponse<DailyCheckMonthlyReport>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MONTHLY_REPORT_URL)
                        .queryParam(HoyolabConstant.PARAM_ACT_ID, HoyolabConstant.ACT_ID_DAILYCHECK)
                        .build())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<DailyCheckMonthlyReport>>() {})
                .block();

        return response.getBody();
    }
}
