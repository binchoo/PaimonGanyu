package org.binchoo.paimonganyu.view.ikakao.type.buttons;

import org.binchoo.paimonganyu.view.ikakao.type.Button;
import org.binchoo.paimonganyu.view.ikakao.type.subtype.Link;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class osLinkButton implements Button {

    @Builder.Default
    private String action = "osLink";
    private Link osLink;
}
