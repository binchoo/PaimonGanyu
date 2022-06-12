package org.binchoo.paimonganyu.chatbot.error.support;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.error.ErrorContext;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.ThrowerAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

/**
 * 다양한 {@link ErrorContextBinder} 구현체를 미리 등록해 놓고
 * 필요할 때 색인하여 꺼내가는 서비스 로케이터.
 * @author jbinchoo
 * @since 2022/06/11
 */
@Slf4j
@Component
public class ErrorContextBinders {

    private final LinkedList<ErrorContextBinder<?>> binders = new LinkedList<>();
    private final Map<Class<? extends ThrowerAware<?>>, ErrorContextBinder<?>> evalCache = new HashMap<>();

    /**
     * 나중에 입력된 {@link ErrorContextBinder}가 더 높은 우선순위를 갖습니다.
     * @param contextBinder 추가할 바인더
     */
    public void add(ErrorContextBinder<?> contextBinder) {
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
    public <T> ErrorContextBinder<T> findBinderFor(Class<? extends ThrowerAware<T>> exceptionType) {
        ErrorContextBinder<T> binder = findBinder(exceptionType);
        if (binder != null)
            return binder;
        throw new IllegalArgumentException("No ErrorContextBinder for " + exceptionType);
    }

    private <T> ErrorContextBinder<T> findBinder(Class<? extends ThrowerAware<T>> exceptionType) {
        if (!alreadyEvaluated(exceptionType)) {
            evaluate(exceptionType);
        }
        return (ErrorContextBinder<T>) evalCache.get(exceptionType);
    }

    private boolean alreadyEvaluated(Class<? extends ThrowerAware<?>> exceptionType) {
        return evalCache.containsKey(exceptionType);
    }

    private <T> void evaluate(Class<? extends ThrowerAware<T>> exceptionType) {
        for (ErrorContextBinder<?> binder : binders) {
            if (binder.canExplain(exceptionType)) {
                evalCache.put(exceptionType, binder);
                break;
            }
        }
    }
}
