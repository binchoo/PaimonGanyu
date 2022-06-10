package org.binchoo.paimonganyu.chatbot.skilldatabind;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.skilldatabind.annotations.ActionParam;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Component
public class ActionParamExtractor extends SkillPayloadValueExtractor {

    public ActionParamExtractor(HttpSession session, ObjectMapper mapper) {
        super(session, mapper);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ActionParam.class);
    }

    @Override
    protected Object resolveArgument(MethodParameter parameter, SkillPayload skillPayload) {
        ActionParam actionParam = parameter.getParameterAnnotation(ActionParam.class);
        if (actionParam != null) {
            String skillParameterKey = actionParam.value();
            return skillPayload.getAction().getParams().get(skillParameterKey);
        }
        return null;
    }
}
