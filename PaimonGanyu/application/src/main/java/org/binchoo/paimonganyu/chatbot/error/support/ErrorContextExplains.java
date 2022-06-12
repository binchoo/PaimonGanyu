package org.binchoo.paimonganyu.chatbot.error.support;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.error.ErrorContextExplain;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 다양한 {@link ErrorContextExplain} 구현체를 미리 등록해 놓고
 * 필요할 때 색인하여 꺼내가는 서비스 로케이터.
 * @author jbinchoo
 * @since 2022/06/11
 */
@Slf4j
@Component
public final class ErrorContextExplains {

    private final LinkedList<ErrorContextExplain> binders = new LinkedList<>();
    private final Map<Class<?>, ErrorContextExplain> evalCache = new HashMap<>();

    /**
     * 나중에 입력된 {@link ErrorContextExplain}가 더 높은 우선순위를 갖습니다.
     * @param contextBinder 추가할 바인더
     */
    public void add(ErrorContextExplain contextBinder) {
        if (contextBinder == null)
            throw new NullPointerException();
        binders.addFirst(contextBinder);
    }

    /**
     * 주어진 클래스를 오류 맥락화할 수 있는 {@link ErrorContextExplain} 반환합니다.
     * @param exceptionType 맥락화 할 클래스
     * @return {@link ErrorContextExplain}
     * @throws IllegalArgumentException 주어진 클래스를 맥락화할 {@link ErrorContextExplain}가 없을 때
     */
    public ErrorContextExplain findByType(Class<?> exceptionType) {
        ErrorContextExplain binder = findBinder(exceptionType);
        if (binder != null)
            return binder;
        throw new IllegalArgumentException("No ErrorContextBinder for " + exceptionType);
    }

    private <T> ErrorContextExplain findBinder(Class<?> exceptionType) {
        if (!alreadyEvaluated(exceptionType)) {
            evaluate(exceptionType);
        }
        return evalCache.get(exceptionType);
    }

    private boolean alreadyEvaluated(Class<?> exceptionType) {
        return evalCache.containsKey(exceptionType);
    }

    private <T> void evaluate(Class<?> exceptionType) {
        for (ErrorContextExplain binder : binders) {
            if (binder.canExplain(exceptionType)) {
                evalCache.put(exceptionType, binder);
                break;
            }
        }
    }
}
