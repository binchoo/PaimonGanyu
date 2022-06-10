package org.binchoo.paimonganyu.ikakao.component.componentType;

import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.Component;
import org.binchoo.paimonganyu.ikakao.type.CarouselHeader;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Carousel implements Component {

    private String type;
    private CarouselHeader header;
    @Singular("addItem")
    private List<CanCarousel> items;

}
