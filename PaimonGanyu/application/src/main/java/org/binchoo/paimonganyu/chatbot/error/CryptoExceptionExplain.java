package org.binchoo.paimonganyu.chatbot.error;

import org.binchoo.paimonganyu.error.ErrorContext;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.FallbackId;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.SecureHoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.exception.CryptoException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Component
public class CryptoExceptionExplain implements ErrorContextBinder<SecureHoyopassCredentials> {

    @Override
    public boolean canExplain(Class<?> exceptionType) {
        return exceptionType.equals(CryptoException.class);
    }

    @Override
    public ErrorContext explain(ThrowerAware<SecureHoyopassCredentials> exception) {
        return new ErrorContext() {
            @Override
            public String getExplanation() {
                return "알 수 없는 암호화 방식입니다.\n옳지 않은 방법으로 만들어진 QR코드 같습니다.";
            }

            @Override
            public Collection<FallbackId> getFallbacks() {
                return null;
            }
        };
    }
}
