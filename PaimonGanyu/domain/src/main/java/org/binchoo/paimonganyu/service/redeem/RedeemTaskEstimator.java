package org.binchoo.paimonganyu.service.redeem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryService;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationService;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<UserHoyopass> users = estimationOption.getUsers();
        List<RedeemCode> codes = estimationOption.getCodes();
        List<RedeemTask> tasks = multiply(users, codes);
        log.debug("Generated tasks: {}", tasks);
        return tasks;
    }

    private List<RedeemTask> multiply(List<UserHoyopass> users, List<RedeemCode> codes) {
        List<RedeemTask> tasks = new ArrayList<>();
        for (UserHoyopass user : users) {
            String userId = user.getBotUserId();
            for (Hoyopass hoyopass : user.getHoyopasses()) {
                String ltuid = hoyopass.getLtuid();
                String cookieToken = hoyopass.getCookieToken();
                for (Uid uid : hoyopass.getUids()) {
                    for (RedeemCode code : codes)
                        if (hasNotRedeemed(userId, ltuid, code)) {
                            tasks.add(RedeemTask.builder()
                                    .botUserId(userId)
                                    .accountId(ltuid)
                                    .cookieToken(cookieToken)
                                    .region(uid.getRegion().lowercase())
                                    .uid(uid.getUidString())
                                    .redeemCode(code)
                                    .build());
                        }
                }
            }
        }
        return tasks;
    }

    private boolean hasNotRedeemed(String botUserId, String ltuid, RedeemCode redeemCode) {
        return redeemHistoryService.hasNotRedeemed(botUserId, ltuid, redeemCode);
    }
}
