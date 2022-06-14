package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.views.dailycheck.DailyCheckListView;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final DailyCheckListView view;

    @Value("${listUserDailyCheck.maxCount}")
    private int maxCount;

    @PostMapping("/hoyopass")
    public SkillResponse dailyCheckPerPass(@RequestBody SkillPayload skillPayload,
                                           Model model) {
        int index = parseIndex(skillPayload);
        String botUserId = parseId(skillPayload);
        UserHoyopass user = findUser(botUserId);

        Hoyopass pass = user.get(index);
        var result = List.of(dailyCheck.claimDailyCheckIn(botUserId, pass));
        log.debug("UserDailyCheck: {}", result);
        return view.renderSkillResponse(List.of(result));
    }

    @PostMapping("/list")
    public SkillResponse listUserDailyCheck(@RequestBody SkillPayload skillPayload,
                                            Model model) {
        String botUserId = parseId(skillPayload);
        UserHoyopass user = findUser(botUserId);

        var result = dailyCheck.historyOfUser(user, maxCount);
        log.debug("UserDailyCheck: {}", result);
        return view.renderSkillResponse(result);
    }

    private String parseId(SkillPayload skillPayload) {
        return skillPayload.getUserRequest().getUser().getId();
    }

    private int parseIndex(SkillPayload skillPayload) {
        Object index = skillPayload.getAction().getClientExtra().get("index");
        if (!(index instanceof Integer))
            throw new RuntimeException();
        return (Integer) index;
    }

    private UserHoyopass findUser(String botUserId) {
        return hoyopassRegister.findUserHoyopass(botUserId);
    }
}
