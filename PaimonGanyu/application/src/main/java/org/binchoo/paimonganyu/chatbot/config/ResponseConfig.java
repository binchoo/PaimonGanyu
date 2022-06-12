package org.binchoo.paimonganyu.chatbot.config;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.error.CryptoExceptionExplain;
import org.binchoo.paimonganyu.chatbot.error.HoyopassExceptionExplain;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorContextBinders;
import org.binchoo.paimonganyu.chatbot.error.support.Fallbacks;
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
    private final ErrorContextBinders binderRegistry;

    @PostConstruct
    public void configure() {
        addQuickReplies(quickReplyRegistry);
        addErrorContextBinders(binderRegistry);
    }

    private void addQuickReplies(QuickReplies quickReplies) {
        quickReplies.add(Fallbacks.Home,
                new QuickReply("처음으로", null, null, null, null));

        quickReplies.add(Fallbacks.ScanHoyopass,
                new QuickReply("재촬영", null, null, null, null));

        quickReplies.add(Fallbacks.DeleteHoyopass,
                new QuickReply("통행증 삭제", null, null, null, null));

        quickReplies.add(Fallbacks.ValidationCs,
                new QuickReply("계정 유효성 안내", null, null, null, null));

        quickReplies.add(Fallbacks.CommonCs,
                new QuickReply("메일 문의", null, null, null, null));
    }

    private void addErrorContextBinders(ErrorContextBinders binders) {
        binders.add(HoyopassExceptionExplain.builder()
                .error(DuplicationException.class)
                .title("이미 등록된 통행증 정보입니다.")
                .fallbacks(Fallbacks.Home, Fallbacks.ScanHoyopass)
                .build());

        binders.add(HoyopassExceptionExplain.builder()
                .error(QuantityException.class)
                .title(String.format("통행증은 %d개까지 보유 가능합니다.", UserHoyopass.MAX_HOYOPASS_COUNT))
                .fallbacks(Fallbacks.Home, Fallbacks.DeleteHoyopass)
                .build());

        binders.add(HoyopassExceptionExplain.builder()
                .error(InactiveStateException.class)
                .title("알 수 없는 통행증 정보입니다.")
                .fallbacks(Fallbacks.Home, Fallbacks.ScanHoyopass, Fallbacks.ValidationCs)
                .build());

        binders.add(CryptoExceptionExplain.builder()
                .text("옳지 않은 방법으로 만들어진 QR 코드인 것 같습니다.")
                .fallbacks(Fallbacks.Home, Fallbacks.ScanHoyopass, Fallbacks.CommonCs)
                .build());
    }
}
