package org.binchoo.paimonganyu.view.ikakao.type.buttons;

import org.binchoo.paimonganyu.view.ikakao.type.Button;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class webLinkButton implements Button {

    @Builder.Default
    private String action = "webLink";
    private String webLinkUrl;
}
