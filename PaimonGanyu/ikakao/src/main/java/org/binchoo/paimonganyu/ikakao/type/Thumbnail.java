package org.binchoo.paimonganyu.ikakao.type;

import org.binchoo.paimonganyu.ikakao.type.subtype.Link;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Thumbnail {

    private String imageUrl;
    private Link link;
    private Boolean fixedRatio;
    private Integer width;
    private Integer height;
}
