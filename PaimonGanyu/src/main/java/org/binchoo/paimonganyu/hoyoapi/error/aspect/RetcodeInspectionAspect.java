package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.binchoo.paimonganyu.hoyoapi.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetcodeInspectionAspect {

    /**
     * <p> 미호요 API는 대부분의 오류 응답 상황에서도 200 상태코드를 줍니다.
     * <p> 대신에 오류 컨텍스트를 {@link org.binchoo.paimonganyu.hoyoapi.HoyoResponse}의 retcode 필드에 담습니다.
     * <p> 이 애프터리터닝 어드바이스는 {@link org.binchoo.paimonganyu.hoyoapi.HoyoResponse}의 retcode 값에 대응하여 예외를 던집니다.
     * @param response {@link org.binchoo.paimonganyu.hoyoapi.webclient} 하위 클라이언트 메서드가 반환한 응답.
     */
    @AfterReturning(
            pointcut = "execution(* org.binchoo.paimonganyu.hoyoapi.webclient.*.*(..))",
            returning = "response")
    public void checkRetcode(HoyoResponse<?> response) {
        int retcode = response.getRetcode();

        RetcodeException retcodeException = RetcodeException.of(retcode);
        String message = response.getMessage();

        if (retcodeException != null) {
            retcodeException.setMessage(message);
            throw retcodeException;
        }
    }
}
