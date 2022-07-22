package org.binchoo.paimonganyu.hoyoapi;

import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRoles;

public interface HoyolabAccountApi extends HoyolabApi {

    /**
     * 호요랩 어카운트 API OS 엔드포인트
     */
    @Override
    default String getBaseUrl() {
        return "https://api-account-os.hoyolab.com/binding/api";
    }

    /**
     * @param ltuidLtoken 유저 통행증 쿠키
     * @return 해당 유저가 보유한 계정 요약의 리스트
     */
    HoyoResponse<UserGameRoles> getUserGameRoles(LtuidLtoken ltuidLtoken);

    /**
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param region 캐릭터가 있는 서버 리젼 ex) "os_asia"
     * @return 해당 유저가 해당 서버에 보유한 계정 요약
     */
    HoyoResponse<UserGameRoles> getUserGameRoleByRegion(LtuidLtoken ltuidLtoken, String region);
}
