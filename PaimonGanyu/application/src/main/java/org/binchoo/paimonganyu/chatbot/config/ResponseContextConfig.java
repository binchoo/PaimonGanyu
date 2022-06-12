package org.binchoo.paimonganyu.chatbot.config;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.error.CryptoExceptionExplain;
import org.binchoo.paimonganyu.chatbot.error.HoyopassExceptionExplain;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorContextBinders;
import org.binchoo.paimonganyu.chatbot.error.support.Fallbacks;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityException;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
@RequiredArgsConstructor
@Configuration
public class ResponseContextConfig {

    private final ErrorContextBinders registry;

    @PostConstruct
    public void addBinders() {
        addErrorContextBinders(registry);
    }

    private void addErrorContextBinders(ErrorContextBinders registry) {
        registry.add(HoyopassExceptionExplain.builder()
                .error(DuplicationException.class)
                .title("이미 등록된 통행증 정보입니다.")
                .fallbacks(Fallbacks.Home, Fallbacks.ScanHoyopass)
                .build());

        registry.add(HoyopassExceptionExplain.builder()
                .error(QuantityException.class)
                .title(String.format("통행증은 %d개까지 보유 가능합니다.", UserHoyopass.MAX_HOYOPASS_COUNT))
                .fallbacks(Fallbacks.Home, Fallbacks.DeleteHoyopass)
                .build());

        registry.add(HoyopassExceptionExplain.builder()
                .error(InactiveStateException.class)
                .title("알 수 없는 통행증 정보입니다.")
                .fallbacks(Fallbacks.Home, Fallbacks.ScanHoyopass, Fallbacks.ValidationCs)
                .build());

        registry.add(CryptoExceptionExplain.builder()
                .text("옳지 않은 방법으로 만들어진 QR 코드인 것 같습니다.")
                .fallbacks(Fallbacks.Home, Fallbacks.ScanHoyopass, Fallbacks.CommonCs)
                .build());
    }
}
