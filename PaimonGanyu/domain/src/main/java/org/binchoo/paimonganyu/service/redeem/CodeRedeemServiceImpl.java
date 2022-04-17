package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserCodeRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserCodeRedeemCrudPort;
import org.binchoo.paimonganyu.redeem.driving.CodeRedeemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CodeRedeemServiceImpl implements CodeRedeemService {

    private final UserCodeRedeemCrudPort repository;

    @Override
    public boolean hasRedeemed(String botUserId, String ltuid, RedeemCode redeemCode) {
        UserCodeRedeem userCodeRedeem = new UserCodeRedeem(botUserId, ltuid, redeemCode);
        List<UserCodeRedeem> histories = repository.findMatches(userCodeRedeem);
        return histories.stream().anyMatch(UserCodeRedeem::isDone);
    }

    @Override
    public boolean hasNotRedeemed(String botUserId, String ltuid, RedeemCode redeemCode) {
        return !this.hasRedeemed(botUserId, ltuid, redeemCode);
    }
}
