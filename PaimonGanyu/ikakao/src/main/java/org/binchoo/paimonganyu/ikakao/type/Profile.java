package org.binchoo.paimonganyu.ikakao.type;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {

    private String title;
    private String imageUrl;
    @Builder.Default
    private int width = 100;
    @Builder.Default
    private int height = 100;
}
