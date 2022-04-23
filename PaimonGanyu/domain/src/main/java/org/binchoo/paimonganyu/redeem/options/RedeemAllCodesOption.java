package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 코드리딤 태스크를 생성하는 옵션입니다.
 * 주어진 유저에게 모든 알려진 리딤 코드를 리딤하는 전략입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemAllCodesOption extends RedeemTaskEstimationOption {

    /**
     * 주어진 유저에게 모든 알려진 리딤 코드를 리딤하는 전략입니다.
     * @param codePort 모든 리딤 코드를 조회하기 위한 포트
     * @param userProvider 리딤 대상 유저 제공자
     */
    public RedeemAllCodesOption(RedeemCodeCrudPort codePort, UserProvider userProvider) {
        this
                .withCodeProvider(codePort::findAll)
                .withUserProvider(userProvider);
    }
}
