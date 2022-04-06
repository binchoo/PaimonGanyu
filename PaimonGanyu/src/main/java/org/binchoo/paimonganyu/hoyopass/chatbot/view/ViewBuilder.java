package org.binchoo.paimonganyu.hoyopass.chatbot.view;

import org.binchoo.paimonganyu.ikakao.SkillResponse;
import org.springframework.ui.Model;

public interface ViewBuilder {
    SkillResponse build(Model viewModel);
}
