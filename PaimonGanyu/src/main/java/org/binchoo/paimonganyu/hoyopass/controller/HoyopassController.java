package org.binchoo.paimonganyu.hoyopass.controller;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.service.HoyopassService;
import org.binchoo.paimonganyu.pojo.SkillPayload;
import org.binchoo.paimonganyu.pojo.SkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/hoyopass")
public class HoyopassController {

    private final HoyopassService hoyopassService;

    @PostMapping("/post")
    public ResponseEntity<SkillResponse> addHoyopass(@RequestBody SkillPayload skillPayload) {
        String secureHoyopass = skillPayload.userRequest.params.get("secureHoyopass");
        String userId = skillPayload.userRequest.user.getId();
        Hoyopass registered = hoyopassService.createHoyopass(userId, secureHoyopass);
        SkillResponse skillResponse = SkillResponse.builder()
                .version("3.0")
//                .template()
                .build();
        return ResponseEntity.ok(skillResponse);
    }

    @PostMapping("/get")
    public SkillResponse getHoyopassList(@RequestBody SkillPayload skillPayload) {
        String userId = skillPayload.userRequest.user.getId();
        return null;
    }

    @PostMapping("/delete")
    public SkillResponse deleteHoyopass(@RequestBody SkillPayload skillPayload) {
        String uid = skillPayload.userRequest.params.get("uid");
        return null;
    }
}
