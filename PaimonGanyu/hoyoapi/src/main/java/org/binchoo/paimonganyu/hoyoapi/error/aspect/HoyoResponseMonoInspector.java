package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Aspect
@Component
public class HoyoResponseMonoInspector {

    private HoyoResponseInspector inspectionDelegate = new HoyoResponseInspector();
    private Set<Retriable> retriableTargets = new HashSet<>();
    private Function<HoyoResponse<?>, Mono<?>> errorMapper = hoyoResponse-> {
        try {
            inspectionDelegate.inspectRetcode(hoyoResponse);
        } catch (Throwable t) {
            return Mono.error(t);
        }
        return Mono.just(hoyoResponse);
    };

    @Around("execution(* org.binchoo.paimonganyu.hoyoapi.webclient.async.*.*(..))")
    public Object inspectAndAppendRetry(ProceedingJoinPoint joinPoint) throws Throwable {
        this.registerTarget(joinPoint);
        Mono<HoyoResponse<?>> responseMono = (Mono<HoyoResponse<?>>) joinPoint.proceed(joinPoint.getArgs());
        return this.addSubcribers(responseMono, errorMapper, getRetriable(joinPoint.getTarget()));
    }

    private Object addSubcribers(Mono<HoyoResponse<?>> responseMono,
                                 Function<HoyoResponse<?>, Mono<?>> retcodeInspector, Retriable retriable) {
        var withException = responseMono.flatMap(retcodeInspector);
        if (retriable != null) {
            return withException.retryWhen(retriable.getRetryObject());
        }
        return withException;
    }

    private void registerTarget(ProceedingJoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (!retriableTargets.contains(target) && target instanceof Retriable) {
            retriableTargets.add((Retriable) target);
        }
    }

    private Retriable getRetriable(Object target) {
        if (retriableTargets.contains(target))
            return (Retriable) target;
        return null;
    }
}
