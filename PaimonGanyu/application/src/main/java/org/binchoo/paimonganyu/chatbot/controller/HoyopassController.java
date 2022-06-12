package org.binchoo.paimonganyu.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorContextBinders;
import org.binchoo.paimonganyu.chatbot.resolver.id.UserId;
import org.binchoo.paimonganyu.chatbot.resolver.param.ActionParam;
import org.binchoo.paimonganyu.chatbot.view.ErrorResponseTemplate;
import org.binchoo.paimonganyu.error.ErrorContext;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driving.SecuredHoyopassRegistryPort;
import org.binchoo.paimonganyu.hoyopass.exception.UserHoyopassException;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.binchoo.paimonganyu.ikakao.component.Component;
import org.binchoo.paimonganyu.ikakao.component.SimpleTextView;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleText;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.binchoo.paimonganyu.ikakao.type.SkillTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HoyopassController {

    private final SecuredHoyopassRegistryPort hoyopassRegistry;

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


    @ExceptionHandler(UserHoyopassException.class)
    public SkillResponse handle(ErrorContextBinders binders, UserHoyopassException e) {
        var binder = binders.findBinderFor(e.getClass());
        var errorContext = binder.explain(e);
        return new ErrorResponseTemplate() {
            @Override
            public SkillResponse build(ErrorContext errorContext) {
                String title = errorContext.getExplanation();
                return SkillResponse.builder()
                        .template(SkillTemplate.builder()
                                .addOutput(new SimpleTextView(new SimpleText(title)))
                                .addQuickReply(new QuickReply("a", "b", "c", "d", null))
                                .build())
                        .build();
            }
        }.build(errorContext);
    }
}
