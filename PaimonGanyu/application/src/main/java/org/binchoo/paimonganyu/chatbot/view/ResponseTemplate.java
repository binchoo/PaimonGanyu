package org.binchoo.paimonganyu.chatbot.view;

import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.ui.ModelMap;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
public interface ResponseTemplate {

    SkillResponse render(ModelMap model);
}
