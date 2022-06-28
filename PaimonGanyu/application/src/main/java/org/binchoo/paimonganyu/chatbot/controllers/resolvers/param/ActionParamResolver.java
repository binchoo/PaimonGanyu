package org.binchoo.paimonganyu.chatbot.controllers.resolvers.param;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.SkillPayloadResolver;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@RequiredArgsConstructor
@Component
public class ActionParamResolver implements HandlerMethodArgumentResolver {

    private final SkillPayloadResolver skillPayloadResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ActionParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ActionParam actionParam = parameter.getParameterAnnotation(ActionParam.class);
        SkillPayload skillPayload = skillPayloadResolver.resolve(webRequest.getNativeRequest(HttpServletRequest.class));
        if (skillPayload != null && actionParam != null) {
            String skillParameterKey = actionParam.value();
            return skillPayload.getAction().getParams().get(skillParameterKey);
        }
        return null;
    }
}
