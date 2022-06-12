package org.binchoo.paimonganyu.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.SecuredHoyopassRegistryPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HoyopassController {

    private final SecuredHoyopassRegistryPort hoyopassRegistry;

    // Argument Resolver가 aws-serverless-java-container에서 동작하지 않고 있으므로
    // SkillPayload 내부의 정보는 직접 추출하도록 합시다.
    @PostMapping("/ikakao/hoyopass/post")
    public ResponseEntity<SkillResponse> addHoyopass(@RequestBody SkillPayload skillPayload) {
        String botUserId = getBotUserId(skillPayload);
        String secureHoyopass = getParameter(skillPayload, "secure_hoyopass");
        UserHoyopass user = hoyopassRegistry.registerHoyopass(botUserId, secureHoyopass);
        log.debug("Registered UserHoyopass: {}", user);
        return null;
    }

    @PostMapping("/ikakao/hoyopass/get")
    public SkillResponse listHoyopasses(@RequestBody SkillPayload skillPayload) {
        return null;
    }

    @PostMapping("/ikakao/hoyopass/delete")
    public SkillResponse deleteHoyopass(@RequestBody SkillPayload skillPayload) {
        return null;
    }

    private String getBotUserId(SkillPayload skillPayload) {
        return skillPayload.getUserRequest().getUser().getId();
    }

    private String getParameter(SkillPayload skillPayload, String key) {
        String param = skillPayload.getAction().getParams().get(key);
        log.debug("Parameter found: {}", param);
        return skillPayload.getAction().getParams().get(key);
    }
}
