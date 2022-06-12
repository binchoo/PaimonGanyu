package org.binchoo.paimonganyu.chatbot.error.binder;

import lombok.Builder;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.FallbackId;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.exception.CryptoException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Builder
public final class CryptoExceptionBinder implements ErrorContextBinder {

    private final String text;
    private final FallbackId[] fallbacks;

    public static class CryptoExceptionBinderBuilder {

        public CryptoExceptionBinderBuilder fallbacks(FallbackId... fallbacks) {
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
            public Collection<FallbackId> getFallbacks() {
                return (fallbacks != null)? List.of(fallbacks) : Collections.emptyList();
            }
        };
    }
}
