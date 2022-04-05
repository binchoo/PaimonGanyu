package org.binchoo.paimonganyu.awsutils.sns;

@FunctionalInterface
public interface FanoutReaction<T> {

    void reactTo(T messagePojo);
}