package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HoyoResponseInspectionAspect {

    /**
     * <p> 미호요 API는 대부분의 오류 응답 상황에서도 200 상태코드를 줍니다.
     * <p> 대신에 오류 컨텍스트가 있다면 {@link HoyoResponse}의 retcode를 0이 아닌 값으로 담습니다.
     * @param response {@link org.binchoo.paimonganyu.hoyoapi.webclient} 하위 클라이언트 메서드가 반환한 응답
     * @throws RetcodeException retcode가 0이 아니며 이 코드에 대응하여 정의된 자손 예외가 있을 때
     */
    @AfterReturning(
            pointcut = "execution(* org.binchoo.paimonganyu.hoyoapi.webclient.*.*(..))",
            returning = "response")
    public void inspectResponse(HoyoResponse<?> response) {
        inspectRetcode(response);
        inspectResponseData(response);
    }

    protected void inspectRetcode(HoyoResponse<?> response) {
        RetcodeException.findMapping(response).ifPresent((ex)-> { throw ex; });
    }

    protected void inspectResponseData(HoyoResponse<?> response) {
        if (response.getData() == null) {
            throw new NullPointerException("HoyoResponse::data에 null이 담겼습니다.");
        }
    }
}
