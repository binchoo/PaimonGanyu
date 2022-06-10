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

    String id;
    String name;
    Map<String, String> params;
    Map<String, DetailParam> detailParams;
    Map<String, Object> clientExtra;
}
