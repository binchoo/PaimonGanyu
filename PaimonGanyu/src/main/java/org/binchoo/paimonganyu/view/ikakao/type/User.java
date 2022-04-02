package org.binchoo.paimonganyu.view.ikakao.type;

import org.binchoo.paimonganyu.view.ikakao.type.subtype.Properties;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class User {

    public Properties properties;

    private String id;
    private String type;
}
