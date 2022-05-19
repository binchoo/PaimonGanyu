package org.binchoo.paimonganyu.awsutils.sqs;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.JsonPayloadAwsEventWrapper;

import java.util.stream.Stream;

public class SQSEventWrapper extends JsonPayloadAwsEventWrapper<SQSEvent> {

    public SQSEventWrapper() {
        super();
    }

    public SQSEventWrapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected Stream<String> getJsonStream(SQSEvent event) {
        return event.getRecords().stream().map(SQSEvent.SQSMessage::getBody);
    }
}
