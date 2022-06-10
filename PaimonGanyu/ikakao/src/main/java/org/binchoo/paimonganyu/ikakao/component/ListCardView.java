package org.binchoo.paimonganyu.ikakao.component;

import org.binchoo.paimonganyu.ikakao.component.componentType.ListCard;

import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListCardView implements Component{

    private ListCard listCard;
}
