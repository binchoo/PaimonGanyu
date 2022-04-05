package org.binchoo.paimonganyu.awsutils.sns;

import java.util.List;

@FunctionalInterface
public interface FanoutBatchReaction<T> {

    void reactTo(List<T> messagePojos);
}