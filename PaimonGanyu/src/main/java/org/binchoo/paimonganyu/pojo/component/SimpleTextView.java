package org.binchoo.paimonganyu.pojo.component;

import org.binchoo.paimonganyu.pojo.component.componentType.SimpleText;
import lombok.*;

@Getter
@Builder
@ToString
public class SimpleTextView implements Component {

    private SimpleText simpleText;
}
