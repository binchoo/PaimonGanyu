package org.binchoo.paimonganyu.ikakao.component;

import org.binchoo.paimonganyu.ikakao.component.componentType.CommerceCard;

import lombok.*;

@Getter
@Builder
@ToString
public class CommerceCardView implements Component, CanCarousel {

    private CommerceCard commerceCard;
}
