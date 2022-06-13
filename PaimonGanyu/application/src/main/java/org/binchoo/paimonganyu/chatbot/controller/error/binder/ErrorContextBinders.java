package org.binchoo.paimonganyu.chatbot.controller.error.binder;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.error.ErrorContextBinder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 다양한 {@link ErrorContextBinder} 구현체를 미리 등록해 놓고
 * 필요할 때 색인하여 꺼내가는 서비스 로케이터.
 * @author jbinchoo
 * @since 2022/06/11
 */
@Slf4j
public final class ErrorContextBinders {

    private final LinkedList<ErrorContextBinder> binders = new LinkedList<>();
    private final Map<Class<?>, ErrorContextBinder> evalCache = new HashMap<>();

    /**
     * 나중에 입력된 {@link ErrorContextBinder}가 더 높은 우선순위를 갖습니다.
     * @param contextBinder 추가할 바인더
     */
    public void add(ErrorContextBinder contextBinder) {
        if (contextBinder == null)
            throw new NullPointerException();
        binders.addFirst(contextBinder);
    }

    /**
     * 주어진 클래스를 오류 맥락화할 수 있는 {@link ErrorContextBinder} 반환합니다.
     * @param exceptionType 맥락화 할 클래스
     * @return {@link ErrorContextBinder}
     * @throws IllegalArgumentException 주어진 클래스를 맥락화할 {@link ErrorContextBinder}가 없을 때
     */
    public ErrorContextBinder findByType(Class<?> exceptionType) {
        ErrorContextBinder binder = findBinder(exceptionType);
        if (binder != null)
            return binder;
        throw new IllegalArgumentException("No ErrorContextBinder for " + exceptionType);
    }

    private <T> ErrorContextBinder findBinder(Class<?> exceptionType) {
        if (!alreadyEvaluated(exceptionType)) {
            evaluate(exceptionType);
        }
        return evalCache.get(exceptionType);
    }

    private boolean alreadyEvaluated(Class<?> exceptionType) {
        return evalCache.containsKey(exceptionType);
    }

    private <T> void evaluate(Class<?> exceptionType) {
        for (ErrorContextBinder binder : binders) {
            if (binder.canExplain(exceptionType)) {
                evalCache.put(exceptionType, binder);
                break;
            }
        }
    }
}
