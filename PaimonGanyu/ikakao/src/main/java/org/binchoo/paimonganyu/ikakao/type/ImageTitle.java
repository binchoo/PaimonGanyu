package org.binchoo.paimonganyu.ikakao.type;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageTitle {

    private String title;
    private String description;
    private String imageUrl;
}
