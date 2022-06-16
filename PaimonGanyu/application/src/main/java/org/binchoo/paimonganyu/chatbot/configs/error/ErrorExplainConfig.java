package org.binchoo.paimonganyu.chatbot.configs.error;

import org.binchoo.paimonganyu.chatbot.errorbinders.CryptoExceptionBinder;
import org.binchoo.paimonganyu.chatbot.errorbinders.HoyopassExceptionBinder;
import org.binchoo.paimonganyu.chatbot.errorbinders.ErrorContextBinders;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityExceedException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityZeroException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
@Configuration
public class ErrorExplainConfig {

    @Bean
    public ErrorContextBinders errorContextBinders() {
        ErrorContextBinders binders = new ErrorContextBinders();

        binders.add(HoyopassExceptionBinder.builder()
                .error(DuplicationException.class)
                .title("이미 등록된 통행증 정보입니다.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ListHoyopass, FallbackMethods.ScanHoyopass)
                .build());

        binders.add(HoyopassExceptionBinder.builder()
                .error(QuantityExceedException.class)
                .title(String.format("통행증은 %d개까지 보유 가능합니다.", UserHoyopass.MAX_HOYOPASS_COUNT))
                .fallbacks(FallbackMethods.Home, FallbackMethods.ListHoyopassAliasDeleteHoyopass)
                .build());

        binders.add(HoyopassExceptionBinder.builder()
                .error(InactiveStateException.class)
                .title("알 수 없는 통행증 정보입니다.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ScanHoyopass, FallbackMethods.ValidationCs)
                .build());

        binders.add(HoyopassExceptionBinder.builder()
                .error(QuantityZeroException.class)
                .title("현재 통행증을 갖고 있지 않습니다.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ScanHoyopassGuide)
                .build());

        binders.add(CryptoExceptionBinder.builder()
                .text("옳지 않은 방법으로 만들어진 QR 코드인 것 같습니다.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ScanHoyopass, FallbackMethods.CommonCs)
                .build());

        return binders;
    }
}
