package org.binchoo.paimonganyu.pojo;

import org.binchoo.paimonganyu.pojo.payload.Action;
import org.binchoo.paimonganyu.pojo.payload.Bot;
import org.binchoo.paimonganyu.pojo.payload.Intent;
import org.binchoo.paimonganyu.pojo.payload.UserRequest;

import lombok.*;

@Getter
@ToString
public class SkillPayload{

    public UserRequest userRequest;
    public Intent intent;

    public Bot bot;
    public Action action;
}

