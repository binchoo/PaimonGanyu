package org.binchoo.paimonganyu.pojo.component;

import org.binchoo.paimonganyu.pojo.component.componentType.SimpleImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimpleImageView implements Component {

    private SimpleImage simpleImage;
}
