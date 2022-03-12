package org.binchoo.paimonganyu.ikakao.component.componentType;

import org.binchoo.paimonganyu.ikakao.component.Component;
import org.binchoo.paimonganyu.ikakao.type.Button;
import org.binchoo.paimonganyu.ikakao.type.ListItem;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
public class ListCard implements Component {

    private ListItem header;
    private List<ListItem> items;
    private List<Button> buttons;
}
