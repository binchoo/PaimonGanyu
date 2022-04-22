package org.binchoo.paimonganyu.hoyoapi;

import org.binchoo.paimonganyu.hoyoapi.pojo.*;
import reactor.core.publisher.Mono;

public interface HoyoCodeRedemptionApi extends HoyolabApi {

    /**
     * hk4e 코드 리딤 API - OS 엔드포인트
     */
    @Override
    default String getBaseUrl() {
        return "https://hk4e-api-os.hoyoverse.com/common/apicdkey/api/webExchangeCdkey";
    }

    /**
     * 코드 리딤을 수행합니다.
     * @param accountIdCookieToken ltuid 및 cookie_token
     * @param uid UID
     * @param server UID 존재하는 서버
     * @param code 리딤 코드
     * @return 코드 리딤 결과
     */
    Mono<HoyoResponse<CodeRedemptionResult>> redeem(AccountIdCookieToken accountIdCookieToken,
                                                    String uid, String server, String code);
}
