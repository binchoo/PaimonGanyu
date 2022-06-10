package org.binchoo.paimonganyu.ikakao.type.buttons;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.Button;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class messageButton implements Button {

    @Builder.Default
    private String action = "message";
    private String messageText;
}
