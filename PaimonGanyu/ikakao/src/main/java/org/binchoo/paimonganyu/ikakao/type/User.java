package org.binchoo.paimonganyu.ikakao.type;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.subtype.Properties;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    public Properties properties;
    private String id;
    private String type;
}
