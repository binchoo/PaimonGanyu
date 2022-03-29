package org.binchoo.paimonganyu.hoyopass.chatbot;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.driving.HoyopassRegistryPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/ikakao/hoyopass")
public class ChatbotController {

    private final HoyopassRegistryPort hoyopassRegistryPort;

    @PostMapping("/post")
    public ResponseEntity<SkillResponse> addHoyopass(@RequestBody SkillPayload skillPayload) {
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
