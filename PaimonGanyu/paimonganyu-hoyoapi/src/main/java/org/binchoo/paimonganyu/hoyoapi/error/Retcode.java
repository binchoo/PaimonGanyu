package org.binchoo.paimonganyu.hoyoapi.error;

import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Retcode {

    /**
     * @return 이 런타임 오류에 대응되는 {@link HoyoResponse}의 retcode 값 여러 개
     */
    int[] codes() default {};
}
