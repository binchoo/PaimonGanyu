package org.binchoo.paimonganyu.chatbot.controller.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.controller.resolver.SkillPayloadResolver;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Slf4j
@RequiredArgsConstructor
@Component
class ServletRequestSkillPayloadResolver implements SkillPayloadResolver {

    private static final String SKILL_PAYLOAD = "SKILL_PAYLOAD";

    private final ObjectMapper mapper;

    @Override
    public SkillPayload resolve(HttpServletRequest request) {
        return resolvePayload(request);
    }

    private SkillPayload resolvePayload(HttpServletRequest request) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            return inputStream.isFinished()? getCachedPayload(request)
                    : cachePayload(request, deserialize(read(inputStream)));
        } catch (IOException e) {
            log.debug("Failed to read request body.", e);
            log.debug("HttpServletRequest: {}", request);
        }
        return null;
    }

    private SkillPayload getCachedPayload(HttpServletRequest request) {
        return (SkillPayload) request.getSession().getAttribute(SKILL_PAYLOAD);
    }

    private SkillPayload cachePayload(HttpServletRequest request, SkillPayload skillPayload) {
        if (skillPayload != null)
            request.getSession().setAttribute(SKILL_PAYLOAD, skillPayload);
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

    private String read(InputStream inputStream) throws IOException {
        String requestBody = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        log.debug("RequestBody: {}", requestBody);
        return requestBody;
    }
}
