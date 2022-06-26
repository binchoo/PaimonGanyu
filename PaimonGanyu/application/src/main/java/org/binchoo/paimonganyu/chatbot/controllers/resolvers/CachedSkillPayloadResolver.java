package org.binchoo.paimonganyu.chatbot.controllers.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.ikakao.SkillPayload;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CachedSkillPayloadResolver implements SkillPayloadResolver {

    private final ObjectMapper objectMapper;

    @Override
    public SkillPayload resolve(HttpServletRequest request) {
        assert request instanceof ContentCachingRequestWrapper;
        return resolvePayload((ContentCachingRequestWrapper) request);
    }

    private SkillPayload resolvePayload(ContentCachingRequestWrapper request) {
        try {
            ServletInputStream is = request.getInputStream();
            return is.isFinished()? deserialize(getCachedContent(request))
                    : deserialize(StreamUtils.copyToByteArray(is));
        } catch (IOException e) {
            log.error("The request body is not a SkillPayload.");
        }
        return null;
    }

    private byte[] getCachedContent(ContentCachingRequestWrapper request) {
        return request.getContentAsByteArray();
    }

    private SkillPayload deserialize(byte[] requestBody) throws IOException {
        try {
            return objectMapper.readValue(requestBody, SkillPayload.class);
        } catch (IOException e) {
            log.debug("Failed to deserialize a request body", e);
            log.debug("Payload: {}", requestBody);
            throw e;
        }
    }
}
