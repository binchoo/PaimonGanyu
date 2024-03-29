package org.binchoo.paimonganyu.chatbot.configs.view;

import org.binchoo.paimonganyu.chatbot.views.error.binders.CryptoExceptionBinder;
import org.binchoo.paimonganyu.chatbot.views.error.binders.ErrorContextBinders;
import org.binchoo.paimonganyu.chatbot.views.error.binders.HoyopassExceptionBinder;
import org.binchoo.paimonganyu.chatbot.views.error.binders.UserExceptionBinder;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.dailycheck.exception.NoUserDailyCheckException;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.*;
import org.binchoo.paimonganyu.redeem.exception.NoUserRedeemException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
@Configuration
public class ErrorConfig {

    @Bean
    public ErrorContextBinders errorContextBinders() {
        ErrorContextBinders binders = new ErrorContextBinders();

        binders.add(UserExceptionBinder.builder()
                .error(DuplicationException.class)
                .title("이미 등록된 통행증 정보입니다.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ListHoyopass, FallbackMethods.ScanHoyopass)
                .build());

        binders.add(UserExceptionBinder.builder()
                .error(ManyHoyopassException.class)
                .title(String.format("통행증은 %d개까지 보유 가능합니다.", UserHoyopass.MAX_HOYOPASS_COUNT))
                .fallbacks(FallbackMethods.Home, FallbackMethods.ListHoyopassAliasDeleteHoyopass)
                .build());

        binders.add(UserExceptionBinder.builder()
                .error(NoHoyopassException.class)
                .title("현재 통행증을 갖고 있지 않습니다.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ScanHoyopassGuide)
                .build());

        binders.add(UserExceptionBinder.builder()
                .error(ImmortalUidException.class)
                .title("지울 수 없는 UID입니다. 통행증은 최소 하나의 UID를 지녀야합니다.")
                .fallbacks(FallbackMethods.Home)
                .build());

        binders.add(HoyopassExceptionBinder.builder()
                .error(InactiveStateException.class)
                .title("여행자를 확인할 수 없습니다. 호요랩 활성 여부를 확인하세요.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ScanHoyopass, FallbackMethods.ValidationCs)
                .build());

        binders.add(UserExceptionBinder.builder()
                .error(NoUserDailyCheckException.class)
                .title("일일 출석을 수행한 이력이 없습니다.")
                .fallbacks(FallbackMethods.Home)
                .build());

        binders.add(UserExceptionBinder.builder()
                .error(NoUserRedeemException.class)
                .title("리딤이 수행된 이력이 없습니다.")
                .fallbacks(FallbackMethods.Home)
                .build());

        binders.add(CryptoExceptionBinder.builder()
                .text("옳지 않은 방법으로 만들어진 QR 코드 같습니다.")
                .fallbacks(FallbackMethods.Home, FallbackMethods.ScanHoyopass, FallbackMethods.CommonCs)
                .build());

        return binders;
    }
}
