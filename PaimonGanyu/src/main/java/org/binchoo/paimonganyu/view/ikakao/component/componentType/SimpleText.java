package org.binchoo.paimonganyu.view.ikakao.component.componentType;

import org.binchoo.paimonganyu.view.ikakao.component.Component;
import lombok.*;

@Getter
@Builder
@ToString
public class SimpleText implements Component {
    private String text;
}
