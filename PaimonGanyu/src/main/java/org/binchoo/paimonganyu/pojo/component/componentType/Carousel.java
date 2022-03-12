package org.binchoo.paimonganyu.pojo.component.componentType;

import org.binchoo.paimonganyu.pojo.component.CanCarousel;
import org.binchoo.paimonganyu.pojo.component.Component;
import org.binchoo.paimonganyu.pojo.type.CarouselHeader;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
public class Carousel implements Component {

    private String type;
    private CarouselHeader header;
    @Singular("addItem")
    private List<CanCarousel> items;

}
