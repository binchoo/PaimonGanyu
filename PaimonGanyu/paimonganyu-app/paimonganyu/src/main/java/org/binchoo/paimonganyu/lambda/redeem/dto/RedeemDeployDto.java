package org.binchoo.paimonganyu.lambda.redeem.dto;

import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemDist;

/**
 * @author : jbinchoo
 * @since : 2022-09-19
 */
public class RedeemDeployDto {

    private String code;
    private String reason;

    public RedeemDeployDto() { }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RedeemDist toDomain() {
        return RedeemDist.builder()
                .code(RedeemCode.of(code))
                .reason(reason)
                .build();
    }
}
