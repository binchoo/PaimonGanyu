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

    private String id;
    private String name;
    private Map<String, String> params;
    private Map<String, DetailParam> detailParams;
    private Map<String, Object> clientExtra;
}
