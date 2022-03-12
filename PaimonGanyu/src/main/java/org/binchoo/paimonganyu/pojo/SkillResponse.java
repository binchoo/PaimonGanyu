package org.binchoo.paimonganyu.pojo;

import org.binchoo.paimonganyu.pojo.type.ContextControl;
import org.binchoo.paimonganyu.pojo.type.SkillTemplate;
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

