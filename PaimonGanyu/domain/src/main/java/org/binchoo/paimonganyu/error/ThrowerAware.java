package org.binchoo.paimonganyu.error;

/**
 * 어떤 오류 발생시, 오류를 겪은 맥락 정보를 담아두고 싶을 떄
 * 해당 오류 클래스이 이 인터페이스를 붙입시다.
 * @author jbinchoo
 * @since 2022/06/11
 */
public interface ThrowerAware<T> {

    /**
     * 오류 발생의 맥락을 설명할 수 있는 객체를 반환합니다. {@link ErrorContextBinder}는
     * 이 메서드가 반환한 정보를 토대로 {@link ErrorContext}를 작성할 책임이 있습니다.
     * @return 이 오류의 발생 맥락에 대하여 정보를 갖고 있는 객체
     */
    T getThrower();

    Throwable getCause();
}
