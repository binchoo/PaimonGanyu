package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.views.dailycheck.DailyCheckTrialListView;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@Slf4j
@RequestMapping("/ikakao/dailycheck")
@RequiredArgsConstructor
@RestController
public class DailyCheckController {

    private final HoyopassRegisterPort hoyopassRegister;
    private final DailyCheckPort dailyCheck;
    private final DailyCheckTrialListView view;

    @Value("${listUserDailyCheck.maxCount}")
    private int maxCount;

    @PostMapping("/list")
    public SkillResponse listUserDailyCheck(@RequestBody SkillPayload skillPayload,
                                      Model model) {
        String botUserId = parseId(skillPayload);
        UserHoyopass user = findUser(botUserId);

        List<List<UserDailyCheck>> userDailyChecks = dailyCheck.historyOfUser(user, maxCount);
        log.debug("UserDailyCheck: {}", userDailyChecks);
        return view.renderSkillResponse(userDailyChecks);
    }

    @PostMapping
    public SkillResponse doDailyCheck(@RequestBody SkillPayload skillPayload,
                                      Model model) {
        String botUserId = parseId(skillPayload);
        UserHoyopass user = findUser(botUserId);

        List<UserDailyCheck> userDailyChecks = dailyCheck.claimDailyCheckIn(user);
        log.debug("UserDailyCheck: {}", userDailyChecks);
        return view.renderSkillResponse(List.of(userDailyChecks));
    }

    private String parseId(SkillPayload skillPayload) {
        return skillPayload.getUserRequest().getUser().getId();
    }

    private UserHoyopass findUser(String botUserId) {
        return hoyopassRegister.findUserHoyopass(botUserId);
    }
}
