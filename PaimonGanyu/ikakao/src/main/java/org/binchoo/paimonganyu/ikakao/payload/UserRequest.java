package org.binchoo.paimonganyu.ikakao.payload;

import lombok.*;
import org.binchoo.paimonganyu.ikakao.type.Block;
import org.binchoo.paimonganyu.ikakao.type.User;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest{

    private Block block;
    private User user;
    private Map<String, String> params;

    private String timezone;
    private String utterance;
    private String lang;

}
