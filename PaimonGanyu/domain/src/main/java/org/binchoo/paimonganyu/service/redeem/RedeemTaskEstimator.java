package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryService;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationService;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedeemTaskEstimator implements RedeemTaskEstimationService {

    private final RedeemHistoryService redeemHistoryService;

    @Override
    public List<RedeemTask> generateTasks(RedeemTaskEstimationOption estimationOption) {
        List<RedeemTask> estimatedTasks = estimationOption.estimateTask();
        List<RedeemTask> filteredTasks = estimatedTasks.stream()
                .filter(this::hasNotRedeemed)
                .collect(Collectors.toList());
        log.debug("Generated tasks: {}", estimatedTasks);
        log.debug("Filtered tasks: {}", filteredTasks);
        return filteredTasks;
    }

    private boolean hasNotRedeemed(RedeemTask redeemTask) {
        return redeemHistoryService.hasNotRedeemed(redeemTask.getBotUserId(),
                redeemTask.getUidString(), redeemTask.getRedeemCode());
    }
}
