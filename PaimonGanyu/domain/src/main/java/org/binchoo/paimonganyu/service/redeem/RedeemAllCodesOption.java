package org.binchoo.paimonganyu.service.redeem;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;

import java.util.ArrayList;
import java.util.List;

/**
 * 코드리딤 태스크를 생성하는 옵션입니다.
 * 주어진 유저에게 모든 알려진 리딤 코드를 리딤하는 전략입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemAllCodesOption implements RedeemTaskEstimationOption {

    private final RedeemCodeCrudPort redeemCodeCrudPort;
    private final List<UserHoyopass> userHoyopasses;

    /**
     * 주어진 유저에게 모든 알려진 리딤 코드를 리딤하는 전략입니다.
     * @param redeemCodeCrudPort 모든 리딤 코드를 조회하기 위한 포트
     * @param givenUserHoyopasses 리딤 대상 유저
     */
    public RedeemAllCodesOption(RedeemCodeCrudPort redeemCodeCrudPort, List<UserHoyopass> givenUserHoyopasses) {
        this.redeemCodeCrudPort = redeemCodeCrudPort;
        this.userHoyopasses = new ArrayList<>();
        this.userHoyopasses.addAll(givenUserHoyopasses);
    }

    @Override
    public List<UserHoyopass> getUsers() {
        return userHoyopasses;
    }

    @Override
    public List<RedeemCode> getCodes() {
        return redeemCodeCrudPort.findAll();
    }
}
