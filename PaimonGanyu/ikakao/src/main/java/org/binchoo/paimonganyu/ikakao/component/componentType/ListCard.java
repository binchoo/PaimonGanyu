package org.binchoo.paimonganyu.ikakao.component.componentType;

import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.Component;
import org.binchoo.paimonganyu.ikakao.type.Button;
import org.binchoo.paimonganyu.ikakao.type.ListItem;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListCard implements Component, CanCarousel {

    private ListItem header;
    @Singular("addListItem")
    private List<ListItem> items;
    @Singular("addButton")
    private List<Button> buttons;
}
