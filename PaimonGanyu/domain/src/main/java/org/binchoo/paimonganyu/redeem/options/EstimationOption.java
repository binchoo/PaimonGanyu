package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;

import java.util.List;

/**
 * 코드 리딤 태스크를 생성하는 전략입니다.
 * 코드 리딤 대상인 유저와 리딤 코드를 반환해야 합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface EstimationOption {

    /**
     * @return 코드 리딤 대상자들
     */
    List<UserHoyopass> getUsers();

    /**
     * @return 코드 리딤 대상 코드들
     */
    List<RedeemCode> getCodes();
}
