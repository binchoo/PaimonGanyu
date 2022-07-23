package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.clientextra.ClientExtra;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.id.UserId;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.param.ActionParam;
import org.binchoo.paimonganyu.chatbot.views.SkillResponseView;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.SecureHoyopassRegisterPort;
import org.binchoo.paimonganyu.ikakao.type.BarcodeData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/ikakao/hoyopass")
@Controller
public class HoyopassController {

    private final SecureHoyopassRegisterPort secureHoyopassRegister;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String addHoyopass(@UserId String botUserId,
                              @ActionParam("secure_hoyopass") BarcodeData barcodeData, Model model) {
        String secureHoyopass = barcodeData.getBarcodeData();

        UserHoyopass user = secureHoyopassRegister.registerHoyopass(botUserId, secureHoyopass);
        model.addAttribute(SkillResponseView.CONTENT_KEY, user.listUids());

        return "uidListView";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String listHoyopasses(@UserId String botUserId, Model model) {
        List<Hoyopass> hoyopasses = secureHoyopassRegister.listHoyopasses(botUserId);
        model.addAttribute(SkillResponseView.CONTENT_KEY, hoyopasses);

        return "hoyopassListView";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteHoyopass(@UserId String botUserId,
                                 @ClientExtra("index") int index, Model model) {
        secureHoyopassRegister.deleteHoyopass(botUserId, index);

        List<Hoyopass> hoyopasses = secureHoyopassRegister.listHoyopasses(botUserId);
        model.addAttribute(SkillResponseView.CONTENT_KEY, hoyopasses);

        return "hoyopassListView";
    }
}
