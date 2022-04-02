package org.binchoo.paimonganyu.view;

import org.binchoo.paimonganyu.view.ikakao.SkillResponse;
import org.springframework.ui.Model;

public interface ViewBuilder {
    SkillResponse build(Model viewModel);
}
