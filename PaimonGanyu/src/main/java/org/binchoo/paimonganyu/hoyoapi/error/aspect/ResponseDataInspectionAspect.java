package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.binchoo.paimonganyu.hoyoapi.response.HoyoResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class ResponseDataInspectionAspect {

    /**
     * <p> 이 애프터리터닝 어드바이스는 {@link HoyoResponse}의 data 값의 null 여부에 검사하여 예외를 던집니다.
     * @param response {@link org.binchoo.paimonganyu.hoyoapi.webclient} 하위 클라이언트 메서드가 반환한 응답.
     */
    @AfterReturning(
            pointcut = "execution(* org.binchoo.paimonganyu.hoyoapi.webclient.*.*(..))",
            returning = "response")
    public void inspectResponseData(HoyoResponse<?> response) {
        if (Objects.isNull(response.getData())) {
            throw new NullPointerException("HoyoResponse.getData()에 null이 담겼습니다.");
        }
    }
}
