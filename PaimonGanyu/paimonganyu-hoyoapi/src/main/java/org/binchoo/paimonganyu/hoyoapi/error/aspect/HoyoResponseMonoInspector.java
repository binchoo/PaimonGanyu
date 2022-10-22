package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.Retriable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * @author : jbinchoo
 * @since : 2022-04-23
 */
@Slf4j
@Aspect
@Component
public class HoyoResponseMonoInspector {

    private final HoyoResponseInspector inspectionDelegate;

    public HoyoResponseMonoInspector() {
        this.inspectionDelegate = new HoyoResponseInspector();
    }

    @Around("execution(* org.binchoo.paimonganyu.hoyoapi.webclient.async.*.*(..))")
    public Object inspectAndAppendRetry(ProceedingJoinPoint joinPoint) throws Throwable {
        Mono<HoyoResponse<?>> responseBody = (Mono<HoyoResponse<?>>) joinPoint.proceed(joinPoint.getArgs());
        return configure(responseBody, joinPoint.getTarget());
    }

    private Object configure(Mono<HoyoResponse<?>> responseBody, Object target) {
        var inspectedBody = attachRetcodeInspection(responseBody);
        return attachRetry(inspectedBody, target);
    }

    private Mono<? extends HoyoResponse<?>> attachRetry(Mono<? extends HoyoResponse<?>> publisher, Object target) {
        if (target instanceof Retriable) {
            Retriable retriable = (Retriable) target;
            Retry r = retriable.getRetry();
            return publisher.retryWhen(r);
        }
        return publisher;
    }

    private Mono<? extends HoyoResponse<?>> attachRetcodeInspection(Mono<HoyoResponse<?>> responseBody) {
        return responseBody.flatMap(body -> {
            try {
                this.inspectionDelegate.inspectRetcode(body);
            } catch (RetcodeException e) {
                log.error("HoyoResponse caused a RetcodeException: {}.", body.getRetcode(), e);
                return Mono.error(e);
            } catch (Exception e) {
                log.error("HoyoResponse caused an Exception.", e);
                return Mono.error(e);
            }
            log.debug("HoyoResponse is OK: {}", body);
            return Mono.just(body);
        });
    }
}
