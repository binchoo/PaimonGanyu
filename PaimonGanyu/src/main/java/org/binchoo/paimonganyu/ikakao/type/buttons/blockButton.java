package org.binchoo.paimonganyu.ikakao.type.buttons;

import org.binchoo.paimonganyu.ikakao.type.Button;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class blockButton implements Button {

    private String label;
    @Builder.Default
    private String action = "block";
    private String messageText;
    private String blockId;
}
