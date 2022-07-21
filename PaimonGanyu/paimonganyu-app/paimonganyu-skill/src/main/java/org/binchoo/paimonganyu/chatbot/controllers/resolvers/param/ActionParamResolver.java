package org.binchoo.paimonganyu.chatbot.controllers.resolvers.param;

import org.binchoo.paimonganyu.chatbot.controllers.resolvers.SkillPayloadResolver;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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
    protected Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType)
            throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {

        ActionParam spec = parameter.getParameterAnnotation(ActionParam.class);
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        return this.getActionParameter(spec, parameter, paramType, servletRequest);
    }

    private Object getActionParameter(ActionParam spec, MethodParameter parameter, Type paramType, HttpServletRequest servletRequest)
            throws IOException, HttpMediaTypeNotSupportedException {

        return (spec == null || servletRequest == null)? null
            : this.getActionParameter(spec.value(), parameter, paramType, servletRequest);
    }

    private Object getActionParameter(String paramKey, MethodParameter parameter, Type paramType, HttpServletRequest servletRequest)
            throws IOException, HttpMediaTypeNotSupportedException {

        SkillPayload payload = skillPayloadResolver.resolve(servletRequest);
        String rawValue = payload.getAction().getParams().get(paramKey);
        HttpInputMessage rawValueHolder = new ServletServerHttpRequest(servletRequest) {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(rawValue.getBytes(StandardCharsets.UTF_8));
            }
        };
        return this.convertToParamType(rawValueHolder, parameter, paramType);
    }

    private Object convertToParamType(HttpInputMessage inputMessage, MethodParameter parameter, Type paramType)
            throws IOException, HttpMediaTypeNotSupportedException {

        return super.readWithMessageConverters(inputMessage, parameter, paramType);
    }
}
