package org.binchoo.paimonganyu.hoyopass;

import lombok.*;

/**
 * @author : jbinchoo
 * @since : 2022-04-22
 */
@EqualsAndHashCode
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HoyopassCredentials {

    private String ltuid;
    private String ltoken;
    private String cookieToken;
}
