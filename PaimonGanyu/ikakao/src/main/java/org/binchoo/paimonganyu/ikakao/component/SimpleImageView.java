package org.binchoo.paimonganyu.ikakao.component;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.component.componentType.SimpleImage;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleImageView implements Component {

    private SimpleImage simpleImage;
}
