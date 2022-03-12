package org.binchoo.paimonganyu.pojo.component;

import org.binchoo.paimonganyu.pojo.component.componentType.BasicCard;
import lombok.*;

@Getter
@Builder
@ToString
public class BasicCardView implements Component, CanCarousel {

    private BasicCard basicCard;
}
