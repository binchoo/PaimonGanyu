package org.binchoo.paimonganyu.chatbot.controllers.resolvers.param;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.SkillPayloadResolver;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Component
public class ActionParamResolver extends RequestResponseBodyMethodProcessor {

    private final SkillPayloadResolver skillPayloadResolver;

    public ActionParamResolver(List<HttpMessageConverter<?>> converters, SkillPayloadResolver skillPayloadResolver) {
        super(converters);
        this.skillPayloadResolver = skillPayloadResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ActionParam.class);
    }

    @Override
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType)
            throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {

        ActionParam actionParam = parameter.getParameterAnnotation(ActionParam.class);
        SkillPayload skillPayload = skillPayloadResolver.resolve(webRequest.getNativeRequest(HttpServletRequest.class));
        if (skillPayload != null && actionParam != null) {
            String skillParameterKey = actionParam.value();
            return skillPayload.getAction().getParams().get(skillParameterKey);
        }
        return null;
    }
}
