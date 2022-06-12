package org.binchoo.paimonganyu.ikakao.type.buttons;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.Button;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlockButton implements Button {

    private String label;
    @Builder.Default
    private String action = "block";
    private String messageText;
    private String blockId;
}
