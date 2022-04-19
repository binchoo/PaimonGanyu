package org.binchoo.paimonganyu.service.redeem;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;

import java.util.ArrayList;
import java.util.List;

/**
 * 코드리딤 태스크를 생성하는 옵션입니다.
 * 모든 유저에게 주어진 코드로 리딤 코드를 리딤하는 전략입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemAllUsersOption implements RedeemTaskEstimationOption {

    private final UserHoyopassCrudPort userHoyopassCrudPort;
    private final List<RedeemCode> redeemCodes;

    /**
     * 모든 유저에게 주어진 코드로 리딤 코드를 리딤하는 전략입니다.
     * @param userHoyopassCrudPort 모든 유저를 조회하기 위한 포트
     * @param givenCodes 리딤에 사용할 코드
     */
    public RedeemAllUsersOption(UserHoyopassCrudPort userHoyopassCrudPort, List<RedeemCode> givenCodes) {
        this.userHoyopassCrudPort = userHoyopassCrudPort;
        this.redeemCodes = new ArrayList<>();
        this.redeemCodes.addAll(givenCodes);
    }

    @Override
    public List<UserHoyopass> getUsers() {
        return userHoyopassCrudPort.findAll();
    }

    @Override
    public List<RedeemCode> getCodes() {
        return redeemCodes;
    }
}
