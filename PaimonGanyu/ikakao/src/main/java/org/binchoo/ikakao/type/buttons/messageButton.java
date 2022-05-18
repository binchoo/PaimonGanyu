package org.binchoo.paimonganyu.ikakao.type.buttons;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.binchoo.paimonganyu.ikakao.type.Button;

@Getter
@Builder
@ToString
public class messageButton implements Button {

    @Builder.Default
    private String action = "message";
    private String messageText;
}
