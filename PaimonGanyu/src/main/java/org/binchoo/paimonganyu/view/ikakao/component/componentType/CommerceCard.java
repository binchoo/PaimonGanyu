package org.binchoo.paimonganyu.view.ikakao.component.componentType;

import org.binchoo.paimonganyu.view.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.view.ikakao.component.Component;
import org.binchoo.paimonganyu.view.ikakao.type.Button;
import org.binchoo.paimonganyu.view.ikakao.type.Profile;
import org.binchoo.paimonganyu.view.ikakao.type.Thumbnail;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
public class CommerceCard implements Component, CanCarousel {

    private String description;
    private Integer price;
    private String currency;
    private Integer discount;
    private Integer discountRate;
    private Integer discountedPrice;
    private List<Thumbnail> thumbnails;
    private Profile profile;
    private List<Button> buttons;
}
