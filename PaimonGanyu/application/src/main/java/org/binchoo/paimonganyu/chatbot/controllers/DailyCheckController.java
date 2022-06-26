package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.clientextra.ClientExtra;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.id.UserId;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@RequestMapping("/ikakao/dailycheck")
@RequiredArgsConstructor
@Controller
public class DailyCheckController {

    private static final String CONTENT_KEY = SkillResponseView.CONTENT_KEY;

    private final DailyCheckPort dailyCheck;
    private final HoyopassRegisterPort hoyopassRegister;

    @Value("${listUserDailyCheck.maxCount}")
    private int maxCount;

    @PostMapping("/hoyopass")
    public String dailyCheckPerPass(@UserId String botUserId,
                                    @ClientExtra("index") int index, Model model) {
        UserHoyopass user = findUser(botUserId);
        Hoyopass pass = user.get(index);

        var result = List.of(dailyCheck.claimDailyCheckIn(botUserId, pass));
        model.addAttribute(CONTENT_KEY, List.of(result));

        return "dailyCheckListView";
    }

    @PostMapping("/list")
    public String listUserDailyCheck(@UserId String botUserId, Model model) {
        UserHoyopass user = findUser(botUserId);

        var result = dailyCheck.historyOfUser(user, maxCount);
        model.addAttribute(CONTENT_KEY, result);

        return "dailyCheckListView";
    }

    private UserHoyopass findUser(String botUserId) {
        return hoyopassRegister.findUserHoyopass(botUserId);
    }
}
