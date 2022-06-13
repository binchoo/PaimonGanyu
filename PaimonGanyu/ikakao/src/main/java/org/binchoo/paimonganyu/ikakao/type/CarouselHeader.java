package org.binchoo.paimonganyu.ikakao.type;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarouselHeader {

    private String title;
    private String description;
    private Thumbnail thumbnail;
}
