package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimation;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryService;
import org.binchoo.paimonganyu.redeem.options.EstimationOption;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@RequiredArgsConstructor
public class RedeemTaskEstimationImpl implements RedeemTaskEstimation {

    private final RedeemHistoryService redeemHistoryService;

    @Override
    public List<RedeemTask> generateTasks(EstimationOption estimationOption) {
        List<UserHoyopass> users = estimationOption.getUsers();
        List<RedeemCode> codes = estimationOption.getCodes();
        return this.multiply(users, codes).stream()
                .filter(this::hasNotRedeemed)
                .collect(Collectors.toList());
    }

    private List<RedeemTask> multiply(List<UserHoyopass> users,
                                      List<RedeemCode> codes) {
        List<RedeemTask> tasks = new ArrayList<>();
        for (UserHoyopass user : users) {
            String userId = user.getBotUserId();
            for (Hoyopass hoyopass : user.getHoyopasses()) {
                String ltuid = hoyopass.getLtuid();
                String ltoken = hoyopass.getLtoken();
                for (RedeemCode code : codes) {
                    tasks.add(new RedeemTask(userId, ltuid, ltoken, code));
                }
            }
        }
        return tasks;
    }

    private boolean hasNotRedeemed(RedeemTask redeemTask) {
        return redeemHistoryService
                .hasNotRedeemed(redeemTask.getBotUserId(),
                        redeemTask.getLtuid(), redeemTask.getRedeemCode());
    }
}
