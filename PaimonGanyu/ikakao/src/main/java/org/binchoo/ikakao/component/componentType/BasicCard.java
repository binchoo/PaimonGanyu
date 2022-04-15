package org.binchoo.paimonganyu.ikakao.component.componentType;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import org.binchoo.paimonganyu.ikakao.component.CanCarousel;
import org.binchoo.paimonganyu.ikakao.component.Component;
import org.binchoo.paimonganyu.ikakao.type.Button;
import org.binchoo.paimonganyu.ikakao.type.Profile;
import org.binchoo.paimonganyu.ikakao.type.Social;
import org.binchoo.paimonganyu.ikakao.type.Thumbnail;

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
