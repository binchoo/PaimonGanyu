package org.binchoo.paimonganyu.service.redeem;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 코드리딤 태스크를 생성하는 옵션입니다.
 * 통행증 당 하나의 uid를 보유하는 유저분들을 대상으로 리딤 코드를 배포합니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemSingleUidUserOption extends RedeemTaskEstimationOption {

    /**
     * 통행증 당 하나의 uid를 보유하는 유저분들을 대상으로 리딤 코드를 배포합니다.
     * @param userPort 유저를 조회하기 위한 포트
     * @param redeemDeployProvider 리딤 배포 명세 제공자
     */
    public RedeemSingleUidUserOption(UserHoyopassCrudPort userPort, RedeemDeployProvider redeemDeployProvider) {
        this
                .withUserProvider(new SingleUidUserProvider(userPort))
                .withDeployProvider(redeemDeployProvider);
    }

    private static class SingleUidUserProvider implements UserProvider {

        private UserHoyopassCrudPort userPort;

        public SingleUidUserProvider(UserHoyopassCrudPort userPort) {
            this.userPort = userPort;
        }

        @Override
        public List<UserHoyopass> provide() {
            List<UserHoyopass> users = userPort.findAll();
            return users.stream().filter(user-> user.size() == user.listUids().size())
                    .collect(Collectors.toList());
        }
    }
}
