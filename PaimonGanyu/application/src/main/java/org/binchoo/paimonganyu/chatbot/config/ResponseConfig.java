package org.binchoo.paimonganyu.chatbot.config;

import com.sun.xml.bind.v2.model.annotation.Quick;
import org.binchoo.paimonganyu.chatbot.error.binder.CryptoExceptionBinder;
import org.binchoo.paimonganyu.chatbot.error.binder.HoyopassExceptionBinder;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorContextBinders;
import org.binchoo.paimonganyu.chatbot.error.support.ErrorFallbacks;
import org.binchoo.paimonganyu.chatbot.view.Images;
import org.binchoo.paimonganyu.chatbot.view.QuickReplies;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityException;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
@PropertySource("classpath:images.properties")
@Configuration
public class ResponseConfig {

    @Bean
    public Images setupImages(@Value("#{${images}}") Map<String, String> images) {
        return new Images(images);
    }

    @Bean
    public QuickReplies setupQuickReplies() {
        QuickReplies quickReplies = new QuickReplies();

        quickReplies.add(ErrorFallbacks.Home,
                new QuickReply("처음으로", "block", null, "62a33634d3ab72600628e825", null));

        quickReplies.add(ErrorFallbacks.ScanHoyopass,
                new QuickReply("재촬영", "block", null, "62a3411cef4e2505c632204e", null));

        quickReplies.add(ErrorFallbacks.DeleteHoyopass,
                new QuickReply("통행증 삭제", "block", null, "62a59a68678c9b1480a53389", null));

        quickReplies.add(ErrorFallbacks.ValidationCs,
                new QuickReply("계정 유효성 안내", "block", null, "62a59aeb678c9b1480a53392", null));

        quickReplies.add(ErrorFallbacks.CommonCs,
                new QuickReply("메일 문의", "block", null, "62a59b0efa834474ed740da6", null));

        return quickReplies;
    }

    @Bean
    public ErrorContextBinders setupErrorContextBinders() {
        ErrorContextBinders binders = new ErrorContextBinders();

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

        return binders;
    }
}
