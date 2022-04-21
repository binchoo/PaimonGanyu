package org.binchoo.paimonganyu.redeem;

/**
 * 리딤 요청 후 응답이 내려올 시 호출될 콜백을 표상합니다.
 * @author : jbinchoo
 * @since : 2022-04-21
 */
@FunctionalInterface
public interface RedeemResultCallback {

    void handleResult(RedeemResult redeemResult);
}
