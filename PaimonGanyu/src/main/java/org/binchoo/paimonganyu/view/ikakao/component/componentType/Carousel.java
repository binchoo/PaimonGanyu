package org.binchoo.paimonganyu.view.ikakao.component.componentType;

import org.binchoo.paimonganyu.view.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.view.ikakao.component.Component;
import org.binchoo.paimonganyu.view.ikakao.type.CarouselHeader;

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
