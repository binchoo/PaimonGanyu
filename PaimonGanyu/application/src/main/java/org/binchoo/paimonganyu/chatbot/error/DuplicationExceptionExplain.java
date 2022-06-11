package org.binchoo.paimonganyu.chatbot.error;

import org.binchoo.paimonganyu.error.FallbackId;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Component
public class DuplicationExceptionExplain extends CommonExceptionExplain {

    @Override
    protected String returnTitle(ThrowerAware<UserHoyopass> exception) {
        return "이미 등록된 통행증 정보입니다.";
    }

    @Override
    protected List<FallbackId> returnFallbacks(ThrowerAware<UserHoyopass> exception) {
        return null;
    }
}
