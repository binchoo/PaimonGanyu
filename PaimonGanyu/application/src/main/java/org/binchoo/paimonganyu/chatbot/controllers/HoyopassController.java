package org.binchoo.paimonganyu.chatbot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.views.hoyopass.ListHoyopassesView;
import org.binchoo.paimonganyu.chatbot.views.uid.ListUidsView;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.SecureHoyopassRegisterPort;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 1. SkillPayload 내부의 정보는 직접 추출하도록 합시다.
 * Argument Resolver가 aws-serverless-java-container 위에서 동작하지 않고 있습니다.
 * 2. View Resolver 역시 동작을 하지 않고 있습니다. 아마 aws-serveless-java-container의 기반은
 * 디스패처 서블릿이 아니며, 서블릿과 스프링 HttpHandler를 어댑테이션 하는 것 같습니다.
 * TODO: Reactive 타입의 컨테이너에서 발생한 두 가지 이슈를 aws-serverless-java-container에 문의한다.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ikakao/hoyopass")
@RestController
public class HoyopassController {

    private final ObjectMapper objectMapper;
    private final SecureHoyopassRegisterPort hoyopassRegistry;
    private final ListUidsView listUidsView;
    private final ListHoyopassesView listHoyopassesView;

    @PostMapping("/post")
    public SkillResponse addHoyopass(@RequestBody SkillPayload skillPayload,
                                     Model model) {
        String botUserId = parseId(skillPayload);
        String secureHoyopass = parseBarcode(skillPayload, "secure_hoyopass");

        UserHoyopass user = hoyopassRegistry.registerHoyopass(botUserId, secureHoyopass);
        return (user != null)? listUidsView.renderSkillResponse(user.listUids()) : null;
    }

    @PostMapping("/list")
    public SkillResponse listHoyopasses(@RequestBody SkillPayload skillPayload,
                                        Model model) {
        String botUserId = parseId(skillPayload);

        List<Hoyopass> hoyopasses = hoyopassRegistry.listHoyopasses(botUserId);
        return listHoyopassesView.renderSkillResponse(hoyopasses);
    }

    @PostMapping("/delete")
    public SkillResponse deleteHoyopass(@RequestBody SkillPayload skillPayload, Model model) {
        String botUserId = parseId(skillPayload);
        int index = parseIndex(skillPayload);

        hoyopassRegistry.deleteHoyopass(botUserId, index);
        return listHoyopasses(skillPayload, null);
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

    private int parseIndex(SkillPayload skillPayload) {
        Object index = skillPayload.getAction().getClientExtra().get("index");
        if (!(index instanceof Integer))
            throw new RuntimeException();
        return (Integer) index;
    }
}
