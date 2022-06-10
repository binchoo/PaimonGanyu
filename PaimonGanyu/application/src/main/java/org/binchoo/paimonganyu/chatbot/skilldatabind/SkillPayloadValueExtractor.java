package org.binchoo.paimonganyu.chatbot.skilldatabind;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.core.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Slf4j
public abstract class SkillPayloadValueExtractor implements HandlerMethodArgumentResolver {

    private static final String SKILL_PAYLOAD_SESSION_KEY = "skillPayload";

    private final HttpSession session;
    private final ObjectMapper mapper;

    protected SkillPayloadValueExtractor(HttpSession session, ObjectMapper mapper) {
        this.session = session;
        this.mapper = mapper;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        SkillPayload skillPayload = resolvePayload(request);
        return (skillPayload == null)? null : resolveArgument(parameter, skillPayload);
    }

    private SkillPayload resolvePayload(HttpServletRequest request) {
        try {
            return useSavedIfConsumed(request);
        } catch (IOException e) {
            log.debug("Failed to read request body.", e);
            log.debug("HttpServletRequest: {}", request);
        }
        return null;
    }

    private SkillPayload useSavedIfConsumed(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        boolean isConsumed = inputStream.isFinished();
        return isConsumed? getSavedPayload()
            : savePayload(deserialize(read(inputStream)));
    }

    private SkillPayload getSavedPayload() {
        return (SkillPayload) session.getAttribute(SKILL_PAYLOAD_SESSION_KEY);
    }

    private SkillPayload savePayload(SkillPayload skillPayload) {
        if (skillPayload != null)
            session.setAttribute(SKILL_PAYLOAD_SESSION_KEY, skillPayload);
        return skillPayload;
    }

    private SkillPayload deserialize(String requestBody) {
        try {
            return mapper.readValue(requestBody, SkillPayload.class);
        } catch (Exception e) {
            log.debug("Failed to deserialize a SkillPayload.", e);
            log.debug("Payload: {}", requestBody);
        }
        return null;
    }

    private String read(ServletInputStream inputStream) throws IOException {
        return StreamUtils.copyToString(inputStream, Charset.defaultCharset());
    }

    public abstract boolean supportsParameter(MethodParameter parameter);

    /**
     * @param parameter {@link MethodParameter} declared in handler method
     * @param skillPayload {@link SkillPayload} read by ObjectMapper - never null
     * @return the resolved value from {@link SkillPayload}
     */
    protected abstract Object resolveArgument(MethodParameter parameter, SkillPayload skillPayload);
}
