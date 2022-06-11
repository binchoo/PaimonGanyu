package org.binchoo.paimonganyu.chatbot.error;

import org.binchoo.paimonganyu.error.ErrorContext;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.FallbackId;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityException;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
public abstract class CommonExceptionExplain implements ErrorContextBinder<UserHoyopass> {

    private static final Set<Class<?>> EXPLAINABLES = Set.of(
            DuplicationException.class, QuantityException.class,
            InactiveStateException.class);

    private final StringBuilder sb = new StringBuilder();

    @Override
    public boolean canExplain(Class<?> exceptionType) {
        return EXPLAINABLES.contains(exceptionType);
    }

    @Override
    public ErrorContext explain(ThrowerAware<UserHoyopass> exception) {
        final UserHoyopass thrower = exception.getThrower();
        return new ErrorContext() {
            @Override
            public String getExplanation() {
                return explanationJoin(returnTitle(exception), thrower.listLtuids());
            }
            @Override
            public Collection<FallbackId> getFallbacks() {
                return returnFallbacks(exception);
            }
        };
    }

    /**
     * @param exception
     * @return 오류 응답 타이틀을 반환합니다.
     */
    protected abstract String returnTitle(ThrowerAware<UserHoyopass> exception);

    /**
     * @param exception
     * @return 오류 대처 수단들의 ID({@link FallbackId})를 반환합니다.
     */
    protected abstract List<FallbackId> returnFallbacks(ThrowerAware<UserHoyopass> exception);

    protected String ltuidJoin(List<String> ltuids) {
        return ltuids.stream().map(ltuid -> String.format("ltuid: %s", ltuid))
                .collect(Collectors.joining("\n"));
    }

    private String explanationJoin(String titleText, List<String> ltuids) {
        sb.setLength(0);
        sb.append(titleText);
        sb.append(ltuidJoin(ltuids));
        return sb.toString();
    }
}
