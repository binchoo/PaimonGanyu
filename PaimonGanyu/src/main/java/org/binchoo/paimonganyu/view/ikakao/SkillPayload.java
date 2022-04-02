package org.binchoo.paimonganyu.view.ikakao;

import org.binchoo.paimonganyu.view.ikakao.payload.Action;
import org.binchoo.paimonganyu.view.ikakao.payload.Bot;
import org.binchoo.paimonganyu.view.ikakao.payload.Intent;
import org.binchoo.paimonganyu.view.ikakao.payload.UserRequest;

import lombok.*;

@Getter
@ToString
public class SkillPayload{

    public UserRequest userRequest;
    public Intent intent;

    public Bot bot;
    public Action action;
}

