package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.RedeemClientPort;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemerService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : jbinchoo
 * @since : 2022-04-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class Redeemer implements RedeemerService {

    private final UserRedeemCrudPort userRedeemCrudPort;
    private final RedeemClientPort redeemApiPort;

    @Override
    public UserRedeem redeem(RedeemTask redeemTask) {
        List<UserRedeem> userRedeems = this.redeem(Collections.singletonList(redeemTask));
        assert userRedeems.size() == 1;
        return userRedeems.get(0);
    }

    @Override
    public List<UserRedeem> redeem(Collection<RedeemTask> redeemTasks) {
        List<UserRedeem> resultContainer = new ArrayList<>();
        redeemApiPort.redeem(redeemTasks, resultContainer::add);
        return userRedeemCrudPort.saveAll(resultContainer);
    }
}
