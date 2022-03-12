package org.binchoo.paimonganyu.pojo.component;

import org.binchoo.paimonganyu.pojo.component.componentType.Carousel;
import lombok.*;


@Getter
@Builder
@ToString
public class CarouselView implements Component{

    private Carousel carousel;
}
