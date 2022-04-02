package org.binchoo.paimonganyu.view.ikakao.component;

import org.binchoo.paimonganyu.view.ikakao.component.componentType.BasicCard;
import lombok.*;

@Getter
@Builder
@ToString
public class BasicCardView implements Component, CanCarousel {

    private BasicCard basicCard;
}
