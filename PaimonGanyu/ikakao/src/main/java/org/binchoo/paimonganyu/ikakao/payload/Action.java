package org.binchoo.paimonganyu.ikakao.payload;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.DetailParam;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Action{

    public String id;
    public String name;
    public Map<String, String> params;
    public Map<String, DetailParam> detailParams;
    public Map<String, Object> clientExtra;
}
