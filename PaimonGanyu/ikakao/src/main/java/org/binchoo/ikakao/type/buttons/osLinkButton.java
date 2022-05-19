package org.binchoo.paimonganyu.ikakao.type.buttons;

import org.binchoo.paimonganyu.ikakao.type.subtype.Link;
import org.binchoo.paimonganyu.ikakao.type.Button;

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
