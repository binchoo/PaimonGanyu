package org.binchoo.paimonganyu.redeem;

import lombok.*;

/**
 * 리딤코드를 표상합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Data
@RequiredArgsConstructor
public class RedeemCode {

    private final String code;

    public static RedeemCode of(String code) {
        return new RedeemCode(code);
    }
}
