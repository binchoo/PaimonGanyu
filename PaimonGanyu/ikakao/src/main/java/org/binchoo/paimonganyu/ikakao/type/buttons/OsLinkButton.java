package org.binchoo.paimonganyu.ikakao.type.buttons;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.Button;
import org.binchoo.paimonganyu.ikakao.type.subtype.Link;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OsLinkButton implements Button {

    @Builder.Default
    private String action = "osLink";
    private Link osLink;
}
