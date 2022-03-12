package org.binchoo.paimonganyu.pojo.component.componentType;

import org.binchoo.paimonganyu.pojo.component.Component;
import lombok.*;

@Getter
@Builder
@ToString
public class SimpleText implements Component {
    private String text;
}
