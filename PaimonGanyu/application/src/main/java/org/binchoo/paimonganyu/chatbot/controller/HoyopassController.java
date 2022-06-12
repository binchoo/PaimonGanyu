package org.binchoo.paimonganyu.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorContextExplains;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorFallbacks;
import org.binchoo.paimonganyu.chatbot.resolver.id.UserId;
import org.binchoo.paimonganyu.chatbot.resolver.param.ActionParam;
import org.binchoo.paimonganyu.chatbot.view.ErrorResponseTemplate;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.error.FallbackId;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.SecuredHoyopassRegistryPort;
import org.binchoo.paimonganyu.hoyopass.exception.CryptoException;
import org.binchoo.paimonganyu.hoyopass.exception.UserHoyopassException;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HoyopassController {

    private final SecuredHoyopassRegistryPort hoyopassRegistry;
    private final ErrorContextExplains binders;
    private final ErrorResponseTemplate errorResponseTemplate;

    @PostMapping("/ikakao/hoyopass/post")
    public ResponseEntity<SkillResponse> addHoyopass(@UserId String botUserId,
                                                     @ActionParam("secure_hoyopass") String secureHoyopass) {
        UserHoyopass registeredHoyopass = hoyopassRegistry.registerHoyopass(botUserId, secureHoyopass);
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

    @ResponseBody
    @ExceptionHandler({CryptoException.class, UserHoyopassException.class})
    public SkillResponse handle(ThrowerAware<?> e) {
        var errorContextBinder = binders.findByType(e.getClass());
        var errorContext = errorContextBinder.explain(e);
        return errorResponseTemplate.build(errorContext);
    }

    @ResponseBody
    @ExceptionHandler
    public SkillResponse handle(Exception e) {
        return errorResponseTemplate.build(new ErrorExplain() {
            @Override
            public String getExplanation() {
                return "알 수 없는 오류가 발생했습니다.";
            }

            @Override
            public Collection<FallbackId> getFallbacks() {
                return List.of(ErrorFallbacks.Home);
            }
        });
    }
}
