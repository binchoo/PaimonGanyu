package org.binchoo.paimonganyu.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.SecuredHoyopassRegistryPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@RestController("/ikakao/hoyopass")
public class HoyopassController {

    private final SecuredHoyopassRegistryPort hoyopassRegistry;

    @PostMapping("/post")
    public ResponseEntity<SkillResponse> addHoyopass(@RequestBody SkillPayload skillPayload) {
        String botUserId = skillPayload.userRequest.user.getId();
        String securedHoyopass = skillPayload.userRequest.params.get("secured_hoyopass");
        UserHoyopass registeredHoyopass = hoyopassRegistry.registerHoyopass(botUserId, securedHoyopass);
        return null;
    }

    @PostMapping("/get")
    public SkillResponse listHoyopasses(@RequestBody SkillPayload skillPayload) {
        return null;
    }

    @PostMapping("/delete")
    public SkillResponse deleteHoyopass(@RequestBody SkillPayload skillPayload) {
        return null;
    }
}
