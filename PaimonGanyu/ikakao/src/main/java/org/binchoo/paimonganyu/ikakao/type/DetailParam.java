package org.binchoo.paimonganyu.ikakao.type;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailParam {

    public String origin;
    public String value;
    public String groupName;
}