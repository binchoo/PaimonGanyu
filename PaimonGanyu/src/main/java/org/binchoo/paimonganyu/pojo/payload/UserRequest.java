package org.binchoo.paimonganyu.pojo.payload;

import org.binchoo.paimonganyu.pojo.type.Block;
import org.binchoo.paimonganyu.pojo.type.User;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
public class UserRequest{

    public Block block;
    public User user;
    public Map<String, String> params;

    private String timezone;
    private String utterance;
    private String lang;

}
