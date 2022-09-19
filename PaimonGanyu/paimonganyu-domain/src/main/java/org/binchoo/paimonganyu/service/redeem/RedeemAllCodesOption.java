package org.binchoo.paimonganyu.service.redeem;

import org.binchoo.paimonganyu.redeem.RedeemDeploy;
import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;

import java.util.stream.Collectors;

/**
 * 코드리딤 태스크를 생성하는 옵션입니다.
 * 주어진 유저에게 모든 알려진 리딤 코드를 리딤하는 전략입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemAllCodesOption extends RedeemTaskEstimationOption {

    private static final String DEPLOY_REASON = "ALL CODE";

    /**
     * 주어진 유저에게 모든 알려진 리딤 코드를 리딤하는 전략입니다.
     * @param codePort 모든 리딤 코드를 조회하기 위한 포트
     * @param userProvider 리딤 대상 유저 제공자
     */
    public RedeemAllCodesOption(RedeemCodeCrudPort codePort, UserProvider userProvider) {
        this
                .withDeployProvider(()-> codePort.findAll().stream()
                        .map(it-> RedeemDeploy.builder().code(it).reason(DEPLOY_REASON).build())
                        .collect(Collectors.toList()))
                .withUserProvider(userProvider);
    }
}
