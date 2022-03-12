package org.binchoo.paimonganyu.hoyopass.api.pojo;

import lombok.*;

@ToString
@Setter
@Getter
@RequiredArgsConstructor
public class LtuidLtoken {

    private final String ltuid;
    private final String ltoken;
}
