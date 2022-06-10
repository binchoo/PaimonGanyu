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
@RestController
public class HoyopassController {

    private final SecuredHoyopassRegistryPort hoyopassRegistry;

    @PostMapping("/ikakao/hoyopass/post")
    public ResponseEntity<SkillResponse> addHoyopass(@RequestBody SkillPayload skillPayload) {
        String botUserId = skillPayload.userRequest.user.getId();
        String securedHoyopass = skillPayload.action.getDetailParams().get("secure_hoyopass").value;
        UserHoyopass registeredHoyopass = hoyopassRegistry.registerHoyopass(botUserId, securedHoyopass);
        log.debug("Registered UserHoyopass: {}", registeredHoyopass);
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
}
