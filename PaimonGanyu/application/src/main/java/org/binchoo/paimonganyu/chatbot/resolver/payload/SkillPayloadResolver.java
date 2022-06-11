package org.binchoo.paimonganyu.chatbot.resolver.payload;

import org.binchoo.paimonganyu.ikakao.SkillPayload;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : jbinchoo
 * @since : 2022-06-11
 */
public interface SkillPayloadResolver {

    /**
     * HttpServletRequest 담긴 SkillPayload를 역직렬화하여 반환한다.
     * @param request - {@link HttpServletRequest}
     * @return the resolved {@link SkillPayload} or {@code null} if the request doesn't have any.
     */
    SkillPayload resolve(HttpServletRequest request);
}
