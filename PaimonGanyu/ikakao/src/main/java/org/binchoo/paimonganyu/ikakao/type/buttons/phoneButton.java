package org.binchoo.paimonganyu.ikakao.type.buttons;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.Button;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class phoneButton implements Button {

    private String label;
    @Builder.Default
    private String action = "phone";
    private String phoneNumber;
}
