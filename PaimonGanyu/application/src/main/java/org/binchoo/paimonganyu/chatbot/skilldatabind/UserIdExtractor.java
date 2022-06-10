package org.binchoo.paimonganyu.chatbot.skilldatabind;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.chatbot.skilldatabind.annotations.UserId;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Component
public class UserIdExtractor extends SkillPayloadValueExtractor {

    public UserIdExtractor(HttpSession session, ObjectMapper mapper) {
        super(session, mapper);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    protected Object resolveArgument(MethodParameter parameter, SkillPayload skillPayload) {
        return skillPayload.getUserRequest().getUser().getId();
    }
}
