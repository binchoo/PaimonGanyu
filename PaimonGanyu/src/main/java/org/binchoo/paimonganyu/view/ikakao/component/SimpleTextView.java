package org.binchoo.paimonganyu.view.ikakao.component;

import org.binchoo.paimonganyu.view.ikakao.component.componentType.SimpleText;
import lombok.*;

@Getter
@Builder
@ToString
public class SimpleTextView implements Component {

    private SimpleText simpleText;
}
