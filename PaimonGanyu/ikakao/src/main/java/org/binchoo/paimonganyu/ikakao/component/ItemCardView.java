package org.binchoo.paimonganyu.ikakao.component;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.component.componentType.ItemCard;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemCardView implements Component{

    private ItemCard itemCard;
}
