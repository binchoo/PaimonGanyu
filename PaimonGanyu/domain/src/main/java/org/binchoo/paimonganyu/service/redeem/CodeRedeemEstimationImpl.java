package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.CodeRedeemTask;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driving.CodeRedeemEstimation;
import org.binchoo.paimonganyu.redeem.driving.CodeRedeemService;
import org.binchoo.paimonganyu.redeem.options.EstimationOption;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
@RequiredArgsConstructor
public class CodeRedeemEstimationImpl implements CodeRedeemEstimation {

    private final CodeRedeemService codeRedeemService;

    @Override
    public List<CodeRedeemTask> generate(EstimationOption estimationOption) {
        List<UserHoyopass> users = estimationOption.getUsers();
        List<RedeemCode> codes = estimationOption.getCodes();
        return this.multiply(users, codes);
    }

    private List<CodeRedeemTask> multiply(List<UserHoyopass> users,
                                          List<RedeemCode> codes) {
        List<CodeRedeemTask> tasks = new ArrayList<>();
        for (UserHoyopass u : users) {
            String botUserId = u.getBotUserId();
            for (Hoyopass h : u.getHoyopasses()) {
                String ltuid = h.getLtuid();
                String ltoken = h.getLtoken();
                for (RedeemCode code : codes) {
                    if (codeRedeemService.hasNotRedeemed(botUserId, ltuid, code)) {
                        tasks.add(new CodeRedeemTask(botUserId, ltuid, ltoken, code));
                    }
                }
            }
        }
        return tasks;
    }
}
