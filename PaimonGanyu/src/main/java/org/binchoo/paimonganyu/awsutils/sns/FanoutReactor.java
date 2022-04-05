package org.binchoo.paimonganyu.awsutils.sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FanoutReactor<T> {

    private final Class<T> clazz;

    private SNSEventWrapper snsEventWrapper;

    public FanoutReactor(SNSEvent snsEvent, Class<T> clazz) {
        this.clazz = clazz;
        this.snsEventWrapper = new SNSEventWrapper(snsEvent);
    }

    /**
     * Reacts to all pojos that are contained in SNSEvent::Record::SNS::Message.
     */
    public void reactToAll(FanoutReaction<T> reaction) {
        this.reactToIf(Objects::nonNull, reaction);
    }

    public void reactToIf(Predicate<? super T> predicate, FanoutReaction<T> reaction) {
        for (T messagePojo : snsEventWrapper.extractPojos(clazz)) {
            if (predicate.test(messagePojo)) {
                reaction.reactTo(messagePojo);
            }
        }
    }

    public void reactToWithBatch(int batchSize, FanoutBatchReaction<T> reaction) {
        List<T> pojos = snsEventWrapper.extractPojos(clazz);
        int s = 0;
        while (s < pojos.size()) {
            reaction.reactTo(pojos.subList(s, batchSize));
            s += batchSize;
        }
    }

    private SNSEventWrapper getOrCreateSNSEventWrapper(SNSEvent snsEvent) {
        if (snsEventWrapper == null) {
            snsEventWrapper = new SNSEventWrapper(snsEvent);
        }
        return snsEventWrapper;
    }
}
