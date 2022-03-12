package org.binchoo.paimonganyu.pojo.component;

import org.binchoo.paimonganyu.pojo.component.componentType.CommerceCard;

import lombok.*;

@Getter
@Builder
@ToString
public class CommerceCardView implements Component, CanCarousel {

    private CommerceCard commerceCard;
}
