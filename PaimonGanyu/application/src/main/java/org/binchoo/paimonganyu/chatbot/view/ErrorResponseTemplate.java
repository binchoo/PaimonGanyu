package org.binchoo.paimonganyu.chatbot.view;

import org.binchoo.paimonganyu.error.ErrorContext;
import org.binchoo.paimonganyu.ikakao.SkillResponse;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
public interface ErrorResponseTemplate {

    SkillResponse build(ErrorContext errorContext);
}
