package org.binchoo.paimonganyu.hoyopass.infra.fanout;

import org.binchoo.paimonganyu.awsutils.sns.AbstractFanoutReactor;

public class UserHoyopassFanoutReactor extends AbstractFanoutReactor<UserHoyopassMessage> {

    private final UserHoyopassMessageReaction messageProcessor;

    public UserHoyopassFanoutReactor(UserHoyopassMessageReaction messageProcessor) {
        super(UserHoyopassMessage.class);
        this.messageProcessor = messageProcessor;
    }

    @Override
    protected void reactEach(UserHoyopassMessage messagePojo) {
        messageProcessor.handle(messagePojo);
    }
}
