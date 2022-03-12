package org.binchoo.paimonganyu.pojo.type;

import org.binchoo.paimonganyu.pojo.type.subtype.Properties;
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
