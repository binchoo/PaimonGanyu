package org.binchoo.paimonganyu.error;

import java.util.Collection;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public interface ErrorContext {

    /**
     * 오류 현상에 대한 설명
     * @return 오류 현상에 대해 설명하는 문자열
     */
    String getExplanation();

    /**
     * 오류 현상에 대처하는 수단들의 아이디 목록
     * @return {@code Collection&lt;FallbackId&gt;}
     */
    Collection<FallbackId> getFallbacks();
}
