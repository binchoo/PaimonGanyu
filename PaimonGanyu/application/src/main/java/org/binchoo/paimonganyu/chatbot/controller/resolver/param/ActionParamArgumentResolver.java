package org.binchoo.paimonganyu.chatbot.controller.resolver.param;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.controller.resolver.SkillPayloadResolver;
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
@Slf4j
@RequiredArgsConstructor
@Component
public class ActionParamArgumentResolver implements HandlerMethodArgumentResolver {

    private final SkillPayloadResolver skillPayloadResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ActionParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ActionParam actionParam = parameter.getParameterAnnotation(ActionParam.class);
        log.debug("Trying to parse {}", parameter);
        SkillPayload skillPayload = skillPayloadResolver.resolve(webRequest.getNativeRequest(HttpServletRequest.class));
        if (skillPayload != null && actionParam != null) {
            String skillParameterKey = actionParam.value();
            return skillPayload.getAction().getParams().get(skillParameterKey);
        }
        return null;
    }
}
