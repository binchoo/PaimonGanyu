package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.webclient.async.Retriable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author : jbinchoo
 * @since : 2022-04-23
 */
@Aspect
@Component
public class HoyoResponseMonoInspector {

    private final HoyoResponseInspector inspectionDelegate;
    private final Set<Retriable> retriableTargets;
    private final Function<HoyoResponse<?>, Mono<?>> errorDetector;

    public HoyoResponseMonoInspector() {
        this.inspectionDelegate = new HoyoResponseInspector();
        this.retriableTargets = new HashSet<>();
        this.errorDetector = hoyoResponse-> {
            try {
                inspectionDelegate.inspectRetcode(hoyoResponse);
            } catch (Exception e) {
                return Mono.error(e);
            }
            return Mono.just(hoyoResponse);
        };
    }

    @Around("execution(* org.binchoo.paimonganyu.hoyoapi.webclient.async.*.*(..))")
    public Object inspectAndAppendRetry(ProceedingJoinPoint joinPoint) throws Throwable {
        registerTarget(joinPoint);
        Mono<HoyoResponse<?>> responseMono = (Mono<HoyoResponse<?>>) joinPoint.proceed(joinPoint.getArgs());
        return addSubscribers(responseMono, errorDetector, getRetriable(joinPoint.getTarget()));
    }

    private void registerTarget(ProceedingJoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (!retriableTargets.contains(target) && target instanceof Retriable) {
            retriableTargets.add((Retriable) target);
        }
    }

    private Object addSubscribers(Mono<HoyoResponse<?>> responseMono,
                                  Function<HoyoResponse<?>, Mono<?>> retcodeInspector, Retriable retriable) {
        Mono<Object> withInspection = responseMono.flatMap(retcodeInspector);
        if (retriable == null)
            return withInspection;
        return withInspection.retryWhen(retriable.getRetryObject());
    }

    private Retriable getRetriable(Object target) {
        if (retriableTargets.contains(target))
            return (Retriable) target;
        return null;
    }
}
