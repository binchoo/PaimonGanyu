package org.binchoo.paimonganyu.pojo.type;

import lombok.*;

@Getter
@Builder
@ToString
public class CarouselHeader {

    private String title;
    private String description;
    private Thumbnail thumbnail;

}
