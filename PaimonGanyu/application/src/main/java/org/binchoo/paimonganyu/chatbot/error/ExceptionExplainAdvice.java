package org.binchoo.paimonganyu.chatbot.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.error.support.DefaultErrorExplain;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorContextExplains;
import org.binchoo.paimonganyu.chatbot.view.error.ErrorResponseTemplate;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.exception.CryptoException;
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

    private final ErrorContextExplains binders;
    private final ErrorResponseTemplate errorResponseTemplate;

    @ExceptionHandler({CryptoException.class, UserHoyopassException.class})
    public ResponseEntity<SkillResponse> handleThrowerAware(ThrowerAware<?> e) {
        log.debug("An error handled by default method: ", e.getCause());
        var errorContext = binders.findByType(e.getClass());
        var errorExplain = errorContext.explain(e);
        return ResponseEntity.ok(errorResponseTemplate.build(errorExplain));
    }

    @ExceptionHandler
    public ResponseEntity<SkillResponse> handleElse(Exception e) {
        log.debug("An error handled by default method", e);
        return ResponseEntity.ok(errorResponseTemplate.build(new DefaultErrorExplain()));
    }
}
