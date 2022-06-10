package org.binchoo.paimonganyu.ikakao.component.componentType;

import org.binchoo.paimonganyu.ikakao.component.Component;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleText implements Component {
    private String text;
}
