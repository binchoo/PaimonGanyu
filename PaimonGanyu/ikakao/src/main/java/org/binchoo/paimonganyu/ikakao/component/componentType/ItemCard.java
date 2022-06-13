package org.binchoo.paimonganyu.ikakao.component.componentType;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.Component;
import org.binchoo.paimonganyu.ikakao.type.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemCard implements Component, CanCarousel {

    private Thumbnail thumbnail;
    private Head head;
    private Profile profile;
    private ImageTitle imageTitle;
    @Singular("addItem")
    private List<ItemList> itemList;
    private String itemListAlignment;
    private ItemListSummary itemListSummary;
    private String title;
    private String description;
    @Singular("addButton")
    private List<Button> buttons;
    private String buttonLayout;
}
