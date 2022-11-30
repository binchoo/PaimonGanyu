package org.binchoo.paimonganyu.chatbot.views.error.binders;

import lombok.Builder;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.exception.CryptoException;

import java.util.Collection;
import java.util.List;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Builder
public final class CryptoExceptionBinder implements ErrorContextBinder {

    private final String text;
    private final FallbackMethod[] fallbacks;

    public static class CryptoExceptionBinderBuilder {

        public CryptoExceptionBinderBuilder fallbacks(FallbackMethod... fallbacks) {
            this.fallbacks = fallbacks;
            return this;
        }
    }

    @Override
    public boolean canExplain(Class<?> exceptionType) {
        return exceptionType.equals(CryptoException.class);
    }

    @Override
    public ErrorExplain explain(ThrowerAware<?> exception) {
        return new ErrorExplain() {
            @Override
            public String getExplanation() {
                return text;
            }

            @Override
            public Collection<FallbackMethod> getFallbacks() {
                return (fallbacks != null)? List.of(fallbacks) : List.of();
            }
        };
    }
}
