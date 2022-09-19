package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.id.UserId;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.chatbot.views.redeem.PassRedeem;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@RequestMapping("/ikakao/redeem")
@RequiredArgsConstructor
@Controller
public class RedeemController {

    private static final String CONTENT_KEY = SkillResponseView.CONTENT_KEY;

    private final RedeemHistoryPort redeemHistory;
    private final HoyopassRegisterPort hoyopassRegister;

    @Value("${listUserRedeem.maxCount}")
    private int maxCount;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String listUserRedeem(@UserId String botUserId, Model model) {
        UserHoyopass user = findUser(botUserId);

        var recentRedeems = redeemHistory.findByUser(user, maxCount);
        model.addAttribute(CONTENT_KEY, PassRedeem.organize(user, recentRedeems, maxCount));

        return "redeemListView";
    }

    @RequestMapping(value = "/text", method = RequestMethod.POST)
    public String listUserRedeemAsText(@UserId String botUserId, Model model) {
        UserHoyopass user = findUser(botUserId);

        var result = redeemHistory.findByUser(user);
        model.addAttribute(CONTENT_KEY, result);

        return "redeemListTextView";
    }

    private UserHoyopass findUser(String botUserId) {
        return hoyopassRegister.findUserHoyopass(botUserId);
    }
}
