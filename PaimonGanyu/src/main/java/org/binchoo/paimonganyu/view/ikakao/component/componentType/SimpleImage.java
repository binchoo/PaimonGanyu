package org.binchoo.paimonganyu.view.ikakao.component.componentType;

import org.binchoo.paimonganyu.view.ikakao.component.Component;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SimpleImage implements Component {

    private String imageUrl;
    private String altText;
}
