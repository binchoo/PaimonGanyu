package org.binchoo.paimonganyu.pojo.type;

import org.binchoo.paimonganyu.pojo.type.subtype.Link;
import lombok.*;

@Getter
@Builder
@ToString
public class ListItem {

    private String title;
    private String description;
    private String imageUrl;
    private Link link;

}
