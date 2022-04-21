package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.redeem.RedeemResult;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driven.RedeemResultCrudPort;
import org.binchoo.paimonganyu.redeem.driven.RedeemClientPort;
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

    private final RedeemResultCrudPort redeemResultCrudPort;
    private final RedeemClientPort redeemApiPort;

    @Override
    public RedeemResult redeem(RedeemTask redeemTask) {
        List<RedeemResult> results = this.redeem(Arrays.asList(redeemTask));
        assert results.size() == 1;
        return results.get(0);
    }

    @Override
    public List<RedeemResult> redeem(Collection<RedeemTask> redeemTasks) {
        final List<RedeemResult> resultContainer = new ArrayList<>();
        redeemApiPort.redeem(redeemTasks, resultContainer::add);
        redeemResultCrudPort.saveAll(resultContainer);
        return Collections.unmodifiableList(resultContainer);
    }
}
