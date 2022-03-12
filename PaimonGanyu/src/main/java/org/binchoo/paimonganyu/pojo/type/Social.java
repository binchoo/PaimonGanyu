package org.binchoo.paimonganyu.pojo.type;

import lombok.*;

@Getter
@Builder
@ToString
public class Social {

    private Long like;
    private Long comment;
    private Long share;
}
