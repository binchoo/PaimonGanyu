package org.binchoo.paimonganyu.ikakao;

import org.binchoo.paimonganyu.ikakao.payload.Action;
import org.binchoo.paimonganyu.ikakao.payload.Bot;
import org.binchoo.paimonganyu.ikakao.payload.Intent;
import org.binchoo.paimonganyu.ikakao.payload.UserRequest;

import lombok.*;

@Getter
@ToString
public class SkillPayload{

    public UserRequest userRequest;
    public Intent intent;

    public Bot bot;
    public Action action;
}

