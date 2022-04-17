package org.binchoo.paimonganyu.redeem.driving;

import org.binchoo.paimonganyu.redeem.RedeemCode;

/**
 * 특정 통행증과 리딤 코드에 대해서 코드 리딤 이력이 존재하는지 여부를 제공합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface CodeRedeemHistoryService {

    /**
     * 주어진 통행증 ID와  리딤 코드 조합으로
     * PaimonGanyu 시스템이 코드 리딤을 진행한 이력이 있는지 확인하고, 해당 이력이 `완수(Done)` 상태인지 확인합니다.
     * @param botUserId 유저 아이디
     * @param ltuid 통행증 ID
     * @param redeemCode 리딤 코드
     * @return 이력의 상태가 완수(Done)로 판단될 경우 true, 아닐 경우 false
     */
    boolean hasRedeemed(String botUserId, String ltuid, RedeemCode redeemCode);

    /**
     * @param botUserId 유저 아이디
     * @param ltuid 통행증 ID
     * @param redeemCode 리딤 코드
     * @return {@link #hasRedeemed}의 인버트
     */
    boolean hasNotRedeemed(String botUserId, String ltuid, RedeemCode redeemCode);
}
