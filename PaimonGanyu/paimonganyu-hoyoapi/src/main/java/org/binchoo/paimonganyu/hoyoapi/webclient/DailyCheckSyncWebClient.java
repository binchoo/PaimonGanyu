package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.binchoo.paimonganyu.hoyoapi.DailyCheckAsyncApi;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckSyncApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckMonthlyReport;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.webclient.async.DailyCheckAsyncWebClient;

public class DailyCheckSyncWebClient implements DailyCheckSyncApi {

    private DailyCheckAsyncApi asyncApi;

    public DailyCheckSyncWebClient() {
        this.asyncApi = new DailyCheckAsyncWebClient();
    }

    /**
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException
     */
    @Override
    public HoyoResponse<DailyCheckResult> claimDailyCheck(LtuidLtoken ltuidLtoken) {
        return this.asyncApi.claimDailyCheck(ltuidLtoken)
                .block();
    }

    /**
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError
     */
    @Override
    public HoyoResponse<DailyCheckMonthlyReport> getDailyCheckStatus(LtuidLtoken ltuidLtoken) {
        return this.asyncApi.getDailyCheckStatus(ltuidLtoken)
                .block();
    }
}
