package org.binchoo.paimonganyu.error.support;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.error.ErrorContextBinder;
import org.binchoo.paimonganyu.error.ThrowerAware;

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
public class ErrorContextBinders {

    private static final LinkedList<ErrorContextBinder<?>> binders = new LinkedList<>();
    private static final Map<Class<? extends ThrowerAware<?>>, ErrorContextBinder<?>> evaluated = new HashMap<>();

    private ErrorContextBinders() { }

    /**
     * 주어진 클래스를 오류 맥락화할 수 있는 {@link ErrorContextBinder} 반환합니다.
     * @param exceptionType 맥락화 할 클래스
     * @return {@link ErrorContextBinder}
     * @throws IllegalArgumentException 주어진 클래스를 맥락화할 {@link ErrorContextBinder}가 없을 때
     */
    public static <T> ErrorContextBinder<T> findInstanceFor(Class<? extends ThrowerAware<T>> exceptionType) {
        ErrorContextBinder<T> binder = findBinder(exceptionType);
        if (binder != null) {
            return binder;
        }
        throw new IllegalArgumentException("No ErrorContextBinder for " + exceptionType);
    }

    private static <T> ErrorContextBinder<T> findBinder(Class<? extends ThrowerAware<T>> exceptionType) {
        if (!evaluated.containsKey(exceptionType)) {
            for (ErrorContextBinder<?> binder : binders) {
                if (binder.canExplain(exceptionType)) {
                    evaluated.put(exceptionType, binder);
                    break;
                }
            }
        }
        return (ErrorContextBinder<T>) evaluated.get(exceptionType);
    }

    /**
     * 나중에 입력된 {@link ErrorContextBinder}가 더 높은 우선순위를 갖습니다.
     * @param contextBinder 추가할 바인더
     */
    public static void addBinder(ErrorContextBinder<?> contextBinder) {
        if (contextBinder == null)
            throw new NullPointerException();
        binders.addFirst(contextBinder);
    }
}
