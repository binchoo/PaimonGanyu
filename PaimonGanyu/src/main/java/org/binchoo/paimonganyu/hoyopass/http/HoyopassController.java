package org.binchoo.paimonganyu.hoyopass.http;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.inport.HoyopassChatbotService;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/hoyopass")
public class HoyopassController {

    private final HoyopassChatbotService hoyopassChatbotService;

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
