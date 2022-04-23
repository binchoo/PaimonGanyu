package org.binchoo.paimonganyu.hoyopass;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author : jbinchoo
 * @since : 2022-04-22
 */
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class HoyopassCredentials {

    private final String ltuid;
    private final String ltoken;
    private final String cookieToken;
}
