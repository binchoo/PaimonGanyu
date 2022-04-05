package org.binchoo.paimonganyu.hoyopass.infra.fanout;

@FunctionalInterface
public interface UserHoyopassMessageReaction {

    void handle(UserHoyopassMessage message);
}
