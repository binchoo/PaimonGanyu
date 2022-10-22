package org.binchoo.paimonganyu.infra.dailycheck.web;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.DailyCheckRequestResult;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckSyncApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DailyCheckClientAdapter implements DailyCheckClientPort {

    private final DailyCheckSyncApi dailyCheckApi;

    @Override
    public DailyCheckRequestResult sendRequest(String ltuid, String ltoken) {
        DailyCheckRequestResult requestResult = new DailyCheckRequestResult();
        try {
            HoyoResponse<DailyCheckResult> response =
                    dailyCheckApi.claimDailyCheck(new LtuidLtoken(ltuid, ltoken));
            requestResult.setMessage(response.getMessage());
        } catch (SignInException e) {
            requestResult.setDuplicated(true);
        } catch (Exception e) {
            requestResult.setError(e);
        }
        return requestResult;
    }
}
