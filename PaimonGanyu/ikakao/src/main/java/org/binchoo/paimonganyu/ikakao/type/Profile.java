package org.binchoo.paimonganyu.ikakao.type;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {

    private String nickname;
    private String imageUrl;
}
