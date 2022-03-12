package org.binchoo.paimonganyu.ikakao.type.buttons;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.binchoo.paimonganyu.ikakao.type.Button;

@Getter
@Builder
@ToString
public class shareButton implements Button {

    private String label;
    @Builder.Default
    private String action = "share";
}
