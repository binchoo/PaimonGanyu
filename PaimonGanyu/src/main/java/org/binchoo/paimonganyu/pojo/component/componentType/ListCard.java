package org.binchoo.paimonganyu.pojo.component.componentType;

import org.binchoo.paimonganyu.pojo.component.Component;
import org.binchoo.paimonganyu.pojo.type.Button;
import org.binchoo.paimonganyu.pojo.type.ListItem;

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
