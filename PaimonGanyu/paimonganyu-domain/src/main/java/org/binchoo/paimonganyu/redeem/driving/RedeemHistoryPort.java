package org.binchoo.paimonganyu.redeem.driving;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;

import java.util.List;

/**
 * 특정 통행증과 리딤 코드를 사용한 코드 리딤 이력이 존재하는지 여부를 제공합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface RedeemHistoryPort {

    /**
     * 주어진 통행증 ID와  리딤 코드 조합으로
     * 코드 리딤을 진행한 이력이 있는지 확인하고, 해당 이력이 {@code 완수(Done)} 상태인지 확인합니다.
     * @param botUserId 유저 아이디
     * @param uid 게임 UID
     * @param redeemCode 리딤 코드
     * @return 이력의 상태가 완수(Done)로 판단될 경우 true, 아닐 경우 false
     */
    boolean hasRedeemed(String botUserId, String uid, RedeemCode redeemCode);

    /**
     * @param botUserId 유저 아이디
     * @param uid 게임 UID
     * @param redeemCode 리딤 코드
     * @return {@link #hasRedeemed}의 인버트
     */
    boolean hasNotRedeemed(String botUserId, String uid, RedeemCode redeemCode);

    List<UserRedeem> findByUser(UserHoyopass user);

    List<UserRedeem> findAll();
}
