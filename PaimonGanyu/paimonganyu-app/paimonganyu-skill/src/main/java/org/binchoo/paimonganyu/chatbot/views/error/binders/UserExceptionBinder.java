package org.binchoo.paimonganyu.chatbot.views.error.binders;

import lombok.Builder;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.ErrorExplain;
import org.binchoo.paimonganyu.error.FallbackMethod;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Builder
public class UserExceptionBinder implements ErrorContextBinder {

    private final Class<?> error;
    private final String title;
    private final FallbackMethod[] fallbacks;

    public static class UserExceptionBinderBuilder {

        public UserExceptionBinderBuilder fallbacks(FallbackMethod... fallbacks) {
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
        ThrowerAware<UserHoyopass> hoyopassException = (ThrowerAware<UserHoyopass>) exception;
        UserHoyopass userHoyopass = hoyopassException.getThrower();
        return new ErrorExplain() {

            @Override
            public String getExplanation() {
                String title = returnTitle(hoyopassException);
                if (userHoyopass != null)
                    return join(title, userHoyopass.listLtuids());
                return title;
            }

            @Override
            public Collection<FallbackMethod> getFallbacks() {
                FallbackMethod[] fallbacks = returnFallbacks(hoyopassException);
                return (fallbacks != null)? List.of(fallbacks) : List.of();
            }
        };
    }

    /**
     * @param exception 오류
     * @return 오류 응답 타이틀을 반환합니다.
     */
    protected String returnTitle(ThrowerAware<UserHoyopass> exception) {
        return this.title;
    }

    /**
     * @param exception 오류
     * @return 오류 대처 수단들의 ID({@link FallbackMethod})를 반환합니다.
     */
    protected FallbackMethod[] returnFallbacks(ThrowerAware<UserHoyopass> exception) {
        return this.fallbacks;
    }

    private static String join(String titleText, List<String> ltuids) {
        return titleText + '\n' + ltuidJoin(ltuids);
    }

    private static String ltuidJoin(List<String> ltuids) {
        return ltuids.stream().map(ltuid -> String.format("ltuid: %s", ltuid))
                .collect(Collectors.joining("\n"));
    }
}
