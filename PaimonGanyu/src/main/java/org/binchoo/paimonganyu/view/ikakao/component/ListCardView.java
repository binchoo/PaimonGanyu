package org.binchoo.paimonganyu.view.ikakao.component;

import org.binchoo.paimonganyu.view.ikakao.component.componentType.ListCard;

import lombok.*;


@Getter
@Builder
@ToString
public class ListCardView implements Component{

    private ListCard listCard;
}
