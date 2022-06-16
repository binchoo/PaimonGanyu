package org.binchoo.paimonganyu.ikakao.component.componentType;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.component.Component;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleImage implements Component {

    private String imageUrl;
    private String altText;
}
