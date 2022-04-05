package org.binchoo.paimonganyu.awsutils.sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;

public abstract class AbstractFanoutReactor<T> {

    private final Class<T> clazz;
    private final SNSRecordMapper snsRecordMapper;

    public AbstractFanoutReactor(Class<T> clazz) {
        this.clazz = clazz;
        this.snsRecordMapper = new SNSRecordMapper();
    }

    /**
     * Reacting to all pojos that are contained in SNSEvent::Record::SNS::Message
     * @param snsEvent the AWS lambda's SNS event
     */
    public void reactAll(SNSEvent snsEvent) {
        for (T messagePojo : snsRecordMapper.extractPojos(snsEvent, clazz)) {
            this.reactEach(messagePojo);
        }
    }

    /**
     * Reacting to each pojo that is contained in one of SNSEvent::Records
     * @param messagePojo the java POJO object deserialized from SNSEvent::Record::SNS::Message
     */
    protected abstract void reactEach(T messagePojo);
}
