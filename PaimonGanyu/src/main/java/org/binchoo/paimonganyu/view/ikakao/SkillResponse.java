package org.binchoo.paimonganyu.view.ikakao;

import org.binchoo.paimonganyu.view.ikakao.type.ContextControl;
import org.binchoo.paimonganyu.view.ikakao.type.SkillTemplate;
import lombok.*;

@Getter
@Builder
@ToString
public class SkillResponse {

    @Builder.Default
    private String version = "2.0";
    private SkillTemplate template;
    private ContextControl context;
    private Object data;
}

