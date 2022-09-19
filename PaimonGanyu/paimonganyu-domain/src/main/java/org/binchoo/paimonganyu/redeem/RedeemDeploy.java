package org.binchoo.paimonganyu.redeem;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 리딤배포를 표상합니다.
 * @author : jbinchoo
 * @since : 2022-09-19
 */
@Data
@Builder
@RequiredArgsConstructor
public class RedeemDeploy {

    private final RedeemCode code;
    private final String reason;
}
