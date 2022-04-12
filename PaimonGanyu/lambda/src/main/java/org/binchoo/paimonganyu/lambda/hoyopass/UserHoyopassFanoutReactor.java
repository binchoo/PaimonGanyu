package org.binchoo.paimonganyu.lambda.hoyopass;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.binchoo.paimonganyu.awsutils.sns.FanoutReactor;

public class UserHoyopassFanoutReactor extends FanoutReactor<UserHoyopassMessage> {

    public UserHoyopassFanoutReactor(SNSEvent snsEvent) {
        super(snsEvent, UserHoyopassMessage.class);
    }
}
