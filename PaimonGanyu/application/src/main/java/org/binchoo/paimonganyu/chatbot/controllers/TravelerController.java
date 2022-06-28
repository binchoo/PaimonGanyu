package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.id.UserId;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.traveler.TravelerStatus;
import org.binchoo.paimonganyu.traveler.driving.TravelerStatusPort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@RequiredArgsConstructor
@RequestMapping("/ikakao/traveler")
@Controller
public class TravelerController {

    private final HoyopassRegisterPort hoyopassRegister;
    private final TravelerStatusPort travelerStatus;

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public String listTravelerStatus(@UserId String botUserId, Model model) {
        UserHoyopass user = hoyopassRegister.findUserHoyopass(botUserId);

        Collection<TravelerStatus> status = travelerStatus.getCurrentStatus(user);
        model.addAttribute(SkillResponseView.CONTENT_KEY, status);

        return "travelerStatusView";
    }
}
