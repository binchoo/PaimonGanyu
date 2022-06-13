package org.binchoo.paimonganyu.chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.view.uid.ListUidsView;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.SecuredHoyopassRegistryPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Map;

@Slf4j
@RequestMapping("/ikakao/hoyopass")
@RequiredArgsConstructor
@Controller
public class HoyopassController {

    private final ObjectMapper objectMapper;
    private final SecuredHoyopassRegistryPort hoyopassRegistry;

    @RequestMapping(method = RequestMethod.POST, value = "/post")
    public String addHoyopass(@RequestBody SkillPayload skillPayload, Model model) {
        // SkillPayload 내부의 정보는 직접 추출하도록 합시다.
        // Argument Resolver가 aws-serverless-java-container 위에서 동작하지 않고 있습니다.
        String botUserId = parseId(skillPayload);
        String secureHoyopass = parseBarcode(skillPayload, "secure_hoyopass");

        UserHoyopass user = hoyopassRegistry.registerHoyopass(botUserId, secureHoyopass);
        if (user != null) {
            model.addAttribute(ListUidsView.UIDS, user.listUids());
            return "ListUidsView";
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    public String listHoyopasses(@RequestBody SkillPayload skillPayload, Model model) {
        return "ListUidsView";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public String deleteHoyopass(@RequestBody SkillPayload skillPayload, Model model) {
        return "SimpleSucessfulView";
    }

    private String parseId(SkillPayload skillPayload) {
        return skillPayload.getUserRequest().getUser().getId();
    }

    private String parseBarcode(SkillPayload skillPayload, String key) {
        String param = getParameter(skillPayload, key);
        try {
            Map<String, String> barcodeData = objectMapper.readValue(param, Map.class);
            return barcodeData.get("barcodeData");
        } catch (Exception e) {
            return null;
        }
    }

    private String getParameter(SkillPayload skillPayload, String key) {
        String param = skillPayload.getAction().getParams().get(key);
        log.debug("Parameter found: {}", param);
        return skillPayload.getAction().getParams().get(key);
    }
}
