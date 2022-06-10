package org.binchoo.paimonganyu.ikakao.type.buttons;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.Button;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class webLinkButton implements Button {

    @Builder.Default
    private String action = "webLink";
    private String webLinkUrl;
}
