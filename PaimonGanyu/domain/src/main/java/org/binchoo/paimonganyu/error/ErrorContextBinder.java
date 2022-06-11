package org.binchoo.paimonganyu.error;

/**
 * {@link ThrowerAware}에게서 얻은 "오류 맥락 정보"를
 * 정형적인 {@link ErrorContext} 객체로 변환합니다.
 * @param <T> 오류 맥락 정보를 갖는 객체
 * @author jbinchoo
 * @since 2022/06/11
 */
public interface ErrorContextBinder<T> {

    /**
     * 이 {@link ErrorContextBinder}가 인자로 주어진 오류 타입의
     * 발생 맥락을 파악할 수 있는지 여부
     * @param exceptionType 오류 타입
     * @return 주어진 오류 타입의 맥락을 파악하여 {@link ErrorContext}로
     * 변환할 수 있으면 {@code true}, 그렇지 않으면 {@code false}
     */
    boolean canExplain(Class<?> exceptionType);

    /**
     * 주어진 오류의 발생 맥락을 설명하는 정형적인 {@link ErrorContext} 객체를 만든다.
     * @param exception 오류 객체
     * @return {@link ErrorContext}
     */
    ErrorContext explain(ThrowerAware<T> exception);
}
