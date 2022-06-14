package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.views.dailycheck.DailyCheckHistoryView;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@RequestMapping("/ikakao/dailycheck")
@RequiredArgsConstructor
@RestController
public class DailyCheckController {

    private final HoyopassRegisterPort hoyopassRegistry;
    private final DailyCheckPort dailyCheck;
    private final DailyCheckHistoryView view;

    @PostMapping
    public void doDailyCheck(@RequestBody SkillPayload skillPayload,
                             Model model) {
        String botUserId = skillPayload.getUserRequest().getUser().getId();

        UserHoyopass user = hoyopassRegistry.findUserHoyopass(botUserId);

    }
}
