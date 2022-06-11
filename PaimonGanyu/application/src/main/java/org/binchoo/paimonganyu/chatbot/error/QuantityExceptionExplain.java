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
public class QuantityExceptionExplain extends CommonExceptionExplain {

    @Override
    protected String returnTitle(ThrowerAware<UserHoyopass> exception) {
        return String.format("통행증은 %d개까지 보유 가능합니다.", UserHoyopass.MAX_HOYOPASS_COUNT);
    }

    @Override
    protected List<FallbackId> returnFallbacks(ThrowerAware<UserHoyopass> exception) {
        return null;
    }
}
