package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.views.dailycheck.DailyCheckHistoryView;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckTrial;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
    private final DailyCheckHistoryView view;

    @PostMapping
    public SkillResponse doDailyCheck(@RequestBody SkillPayload skillPayload,
                                      Model model) {
        String botUserId = skillPayload.getUserRequest().getUser().getId();

        UserHoyopass user = hoyopassRegister.findUserHoyopass(botUserId);
        Collection<UserDailyCheckTrial> trials = dailyCheck.claimDailyCheckIn(user);

        log.debug("UserDailyCheck: {}", trials);
        return view.renderSkillResponse(trials);
    }
}
