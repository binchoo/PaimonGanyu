package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.RedemptionClientPort;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedemptionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class Redeemer implements RedemptionService {

    private final UserRedeemCrudPort userRedeemCrudPort;
    private final RedemptionClientPort redeemApiPort;

    @Override
    public UserRedeem redeem(RedeemTask redeemTask) {
        List<UserRedeem> result = this.redeem(Collections.singletonList(redeemTask));
        assert result.size() == 1;
        return result.get(0);
    }

    @Override
    public List<UserRedeem> redeem(Collection<RedeemTask> redeemTasks) {
        List<UserRedeem> result = redeemApiPort.redeem(redeemTasks, null);
        return userRedeemCrudPort.saveAll(result);
    }
}
