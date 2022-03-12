package org.binchoo.paimonganyu.pojo.component.componentType;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import org.binchoo.paimonganyu.pojo.component.CanCarousel;
import org.binchoo.paimonganyu.pojo.component.Component;
import org.binchoo.paimonganyu.pojo.type.Button;
import org.binchoo.paimonganyu.pojo.type.Profile;
import org.binchoo.paimonganyu.pojo.type.Social;
import org.binchoo.paimonganyu.pojo.type.Thumbnail;

import java.util.List;

@Getter
@Builder
@ToString
public class BasicCard implements Component, CanCarousel {

    private String title;
    private String description;
    private Thumbnail thumbnaill;
    private Profile profile;
    private Social social;

    @Singular("addButton")
    private List<Button> buttons;
}

