package org.binchoo.paimonganyu.lambda.fanout;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.binchoo.paimonganyu.awsutils.sns.FanoutReactor;
import org.binchoo.paimonganyu.lambda.dailycheck.dto.UserHoyopassMessage;

public class UserHoyopassFanoutReactor extends FanoutReactor<UserHoyopassMessage> {

    public UserHoyopassFanoutReactor(SNSEvent snsEvent) {
        super(snsEvent, UserHoyopassMessage.class);
    }
}
