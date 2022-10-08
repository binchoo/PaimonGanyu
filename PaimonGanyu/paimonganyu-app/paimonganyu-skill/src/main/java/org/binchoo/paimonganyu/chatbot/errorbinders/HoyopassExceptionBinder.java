package org.binchoo.paimonganyu.chatbot.errorbinders;

import lombok.Builder;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Builder
public class HoyopassExceptionBinder implements ErrorContextBinder {

    private final Class<?> error;
    private final String title;
    private final FallbackMethod[] fallbacks;

    public static class HoyopassExceptionBinderBuilder {

        public HoyopassExceptionBinderBuilder fallbacks(FallbackMethod... fallbacks) {
            this.fallbacks = fallbacks;
            return this;
        }
    }

    @Override
    public boolean canExplain(Class<?> exceptionType) {
        return error.equals(exceptionType);
    }

    @Override
    public ErrorExplain explain(ThrowerAware<?> exception) {
        ThrowerAware<Hoyopass> ex = (ThrowerAware<Hoyopass>) exception;
        Hoyopass hoyopass = ex.getThrower();
        return new ErrorExplain() {

            @Override
            public String getExplanation() {
                if (hoyopass != null)
                    return String.format("%s\n ltuid: %s", title, hoyopass.getLtuid());
                return title;
            }

            @Override
            public Collection<FallbackMethod> getFallbacks() {
                return (fallbacks != null)? List.of(fallbacks) : List.of();
            }
        };
    }
}
