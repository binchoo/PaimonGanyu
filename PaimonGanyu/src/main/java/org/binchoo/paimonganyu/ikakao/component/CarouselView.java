package org.binchoo.paimonganyu.ikakao.component;

import org.binchoo.paimonganyu.ikakao.component.componentType.Carousel;
import lombok.*;


@Getter
@Builder
@ToString
public class CarouselView implements Component{

    private Carousel carousel;
}
