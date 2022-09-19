package org.binchoo.paimonganyu.service.redeem;

import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;

/**
 * 코드리딤 태스크를 생성하는 옵션입니다.
 * 모든 유저에게 리딤 배포에 담긴 리딤 코드를 배포하는 전략입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemAllUsersOption extends RedeemTaskEstimationOption {

    /**
     * 모든 유저에게 주어진 코드로 리딤 코드를 리딤하는 전략입니다.
     * @param userPort 모든 유저를 조회하기 위한 포트
     * @param redeemDeployProvider 리딤 배포 명세 제공자
     */
    public RedeemAllUsersOption(UserHoyopassCrudPort userPort, RedeemDeployProvider redeemDeployProvider) {
        this
                .withUserProvider(userPort::findAll)
                .withDeployProvider(redeemDeployProvider);
    }
}
