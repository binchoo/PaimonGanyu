package org.binchoo.paimonganyu.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.views.error.binders.ErrorContextBinders;
import org.binchoo.paimonganyu.chatbot.views.error.DefaultErrorExplain;
import org.binchoo.paimonganyu.chatbot.views.error.ErrorResponseView;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.exception.CryptoException;
import org.binchoo.paimonganyu.hoyopass.exception.HoyopassException;
import org.binchoo.paimonganyu.hoyopass.exception.UserHoyopassException;
import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionExplainAdvice {

    private final ErrorContextBinders binders;
    private final ErrorResponseView errorResponseView;

    @ExceptionHandler({UserHoyopassException.class, HoyopassException.class, CryptoException.class})
    public ResponseEntity<SkillResponse> handleThrowerAware(ThrowerAware<?> e) {
        log.debug("An error handled will be handled: ", e.getCause());
        ErrorContextBinder ecb = binders.findByType(e.getClass());
        ErrorExplain explain = ecb.explain(e);
        return ResponseEntity.ok(errorResponseView.build(explain));
    }

    @ExceptionHandler
    public ResponseEntity<SkillResponse> handleElse(Exception e) {
        log.debug("An error handled will be handled: ", e);
        ErrorExplain explain = new DefaultErrorExplain();
        return ResponseEntity.ok(errorResponseView.build(explain));
    }
}
