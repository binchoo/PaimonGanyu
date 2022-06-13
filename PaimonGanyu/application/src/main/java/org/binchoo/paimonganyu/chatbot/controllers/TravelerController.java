package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.views.traveler.TravelerStatusView;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegistryPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.traveler.TravelerStatus;
import org.binchoo.paimonganyu.traveler.driving.TravelerStatusPort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@RequiredArgsConstructor
@RequestMapping("/ikakao/traveler")
@RestController
public class TravelerController {

    private final HoyopassRegistryPort hoyopassRegistry;
    private final TravelerStatusPort travelerStatus;
    private final TravelerStatusView view;

    @PostMapping("/status")
    public SkillResponse listTravelerStatus(@RequestBody SkillPayload skillPayload,
                                            Model model) {
        String botUserId = skillPayload.getUserRequest().getUser().getId();

        UserHoyopass user = hoyopassRegistry.findUserHoyopass(botUserId);
        Collection<TravelerStatus> status = travelerStatus.getCurrentStatus(user);
        assert !status.isEmpty();
        return view.renderSkillResponse(status);
    }
}
