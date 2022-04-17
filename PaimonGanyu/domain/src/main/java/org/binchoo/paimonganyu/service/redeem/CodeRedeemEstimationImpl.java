package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.CodeRedeemTask;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driving.CodeRedeemEstimation;
import org.binchoo.paimonganyu.redeem.driving.CodeRedeemHistoryService;
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
public class CodeRedeemEstimationImpl implements CodeRedeemEstimation {

    private final CodeRedeemHistoryService codeRedeemHistoryService;

    @Override
    public List<CodeRedeemTask> generate(EstimationOption estimationOption) {
        List<UserHoyopass> users = estimationOption.getUsers();
        List<RedeemCode> codes = estimationOption.getCodes();
        return this.multiply(users, codes).stream()
                .filter(this::deduplication)
                .collect(Collectors.toList());
    }

    private List<CodeRedeemTask> multiply(List<UserHoyopass> users,
                                          List<RedeemCode> codes) {
        List<CodeRedeemTask> tasks = new ArrayList<>();
        for (UserHoyopass user : users) {
            String userId = user.getBotUserId();
            for (Hoyopass hoyopass : user.getHoyopasses()) {
                String ltuid = hoyopass.getLtuid();
                String ltoken = hoyopass.getLtoken();
                for (RedeemCode code : codes) {
                    tasks.add(new CodeRedeemTask(userId, ltuid, ltoken, code));
                }
            }
        }
        return tasks;
    }

    private boolean deduplication(CodeRedeemTask codeRedeemTask) {
        return codeRedeemHistoryService
                .hasNotRedeemed(codeRedeemTask.getBotUserId(),
                        codeRedeemTask.getLtuid(), codeRedeemTask.getRedeemCode());
    }
}
