package org.binchoo.paimonganyu.hoyoapi.apis;

import org.binchoo.paimonganyu.hoyoapi.HoyolabApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckMonthlyReport;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;

public interface HoyolabDailyCheckApi extends HoyolabApi {

    @Override
    default String getBaseUrl() {
        return "https://hk4e-api-os.mihoyo.com/event/sol";
    }

    /**
     * 이 유저에 대해서 일일 출석 체크를 수행합니다.
     * @param ltuidLtoken 유저 통행증 쿠키
     * @return 오늘자 일일 체크의 성패 응답
     */
    HoyoResponse<DailyCheckResult> claimDailyCheck(LtuidLtoken ltuidLtoken);


    /**
     * 이 유저가 이번 달 일일 출석 체크한 현황을 얻습니다.
     * @param ltuidLtoken 유저 통행증 쿠키
     * @return 이번달 일일 체크 현황
     */
    HoyoResponse<DailyCheckMonthlyReport> getDailyCheckStatus(LtuidLtoken ltuidLtoken);
}
