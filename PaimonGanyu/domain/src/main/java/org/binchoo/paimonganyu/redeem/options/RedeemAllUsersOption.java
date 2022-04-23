package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 코드리딤 태스크를 생성하는 옵션입니다.
 * 모든 유저에게 주어진 코드로 리딤 코드를 리딤하는 전략입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class RedeemAllUsersOption extends RedeemTaskEstimationOption {

    /**
     * 모든 유저에게 주어진 코드로 리딤 코드를 리딤하는 전략입니다.
     * @param userPort 모든 유저를 조회하기 위한 포트
     * @param codeProvider 리딤에 사용할 코드 제공자
     */
    public RedeemAllUsersOption(UserHoyopassCrudPort userPort, CodeProvider codeProvider) {
        this
                .withUserProvider(userPort::findAll)
                .withCodeProvider(codeProvider);
    }
}
