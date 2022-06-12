package org.binchoo.paimonganyu.chatbot.config;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.error.binder.CryptoExceptionBinder;
import org.binchoo.paimonganyu.chatbot.error.binder.HoyopassExceptionBinder;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorContextExplains;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorFallbacks;
import org.binchoo.paimonganyu.chatbot.view.QuickReplies;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityException;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
@RequiredArgsConstructor
@Configuration
public class ResponseConfig {

    private final QuickReplies quickReplyRegistry;
    private final ErrorContextExplains binderRegistry;

    @PostConstruct
    public void configure() {
        addQuickReplies(quickReplyRegistry);
        addErrorContextBinders(binderRegistry);
    }

    private void addQuickReplies(QuickReplies quickReplies) {
        quickReplies.add(ErrorFallbacks.Home,
                new QuickReply("처음으로", null, null, null, null));

        quickReplies.add(ErrorFallbacks.ScanHoyopass,
                new QuickReply("재촬영", null, null, null, null));

        quickReplies.add(ErrorFallbacks.DeleteHoyopass,
                new QuickReply("통행증 삭제", null, null, null, null));

        quickReplies.add(ErrorFallbacks.ValidationCs,
                new QuickReply("계정 유효성 안내", null, null, null, null));

        quickReplies.add(ErrorFallbacks.CommonCs,
                new QuickReply("메일 문의", null, null, null, null));
    }

    private void addErrorContextBinders(ErrorContextExplains binders) {
        binders.add(HoyopassExceptionBinder.builder()
                .error(DuplicationException.class)
                .title("이미 등록된 통행증 정보입니다.")
                .fallbacks(ErrorFallbacks.Home, ErrorFallbacks.ScanHoyopass)
                .build());

        binders.add(HoyopassExceptionBinder.builder()
                .error(QuantityException.class)
                .title(String.format("통행증은 %d개까지 보유 가능합니다.", UserHoyopass.MAX_HOYOPASS_COUNT))
                .fallbacks(ErrorFallbacks.Home, ErrorFallbacks.DeleteHoyopass)
                .build());

        binders.add(HoyopassExceptionBinder.builder()
                .error(InactiveStateException.class)
                .title("알 수 없는 통행증 정보입니다.")
                .fallbacks(ErrorFallbacks.Home, ErrorFallbacks.ScanHoyopass, ErrorFallbacks.ValidationCs)
                .build());

        binders.add(CryptoExceptionBinder.builder()
                .text("옳지 않은 방법으로 만들어진 QR 코드인 것 같습니다.")
                .fallbacks(ErrorFallbacks.Home, ErrorFallbacks.ScanHoyopass, ErrorFallbacks.CommonCs)
                .build());
    }
}
