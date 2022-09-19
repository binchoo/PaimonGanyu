package org.binchoo.paimonganyu.redeem;

import lombok.*;

/**
 * 리딤코드를 표상합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedeemCode {

    private String code;

    public static RedeemCode of(String code) {
        return new RedeemCode(code);
    }
}
