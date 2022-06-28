package org.binchoo.paimonganyu.error;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 오류 대처 수단의 고유 지칭(아이디)를 표상합니다.
 * @author jbinchoo
 * @since 2022/06/12
 */
@Data
@RequiredArgsConstructor
public class FallbackMethod {

    private final String id;
}
