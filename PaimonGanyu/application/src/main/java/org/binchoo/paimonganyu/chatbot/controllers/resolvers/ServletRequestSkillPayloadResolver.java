package org.binchoo.paimonganyu.chatbot.controllers.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ServletRequestSkillPayloadResolver implements SkillPayloadResolver {

    private final ObjectMapper mapper;

    @Override
    public SkillPayload resolve(HttpServletRequest request) {
        return resolvePayload(request);
    }

    private SkillPayload resolvePayload(HttpServletRequest request) {
        assert request instanceof ContentCachingRequestWrapper;
        return deserialize(getCachedContent((ContentCachingRequestWrapper) request));
    }

    private byte[] getCachedContent(ContentCachingRequestWrapper request) {
        return request.getContentAsByteArray();
    }

    private SkillPayload deserialize(byte[] requestBody) {
        try {
            return mapper.readValue(requestBody, SkillPayload.class);
        } catch (Exception e) {
            log.debug("Failed to deserialize a SkillPayload.", e);
            log.debug("Payload: {}", requestBody);
        }
        return null;
    }
}
