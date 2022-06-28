package org.binchoo.paimonganyu.chatbot.controllers.resolvers.clientextra;

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
public class ClientExtraResolver implements HandlerMethodArgumentResolver {

    private final SkillPayloadResolver skillPayloadResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ClientExtra.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ClientExtra clientExtra = parameter.getParameterAnnotation(ClientExtra.class);
        SkillPayload skillPayload = skillPayloadResolver.resolve(webRequest.getNativeRequest(HttpServletRequest.class));
        if (skillPayload != null && clientExtra != null) {
            String key = clientExtra.value();
            return skillPayload.getAction().getClientExtra().get(key);
        }
        return null;
    }
}
