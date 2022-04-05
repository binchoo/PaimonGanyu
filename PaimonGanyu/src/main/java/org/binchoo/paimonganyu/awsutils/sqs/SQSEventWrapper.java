package org.binchoo.paimonganyu.awsutils.sqs;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.JsonPayloadAwsEventWrapper;

import java.util.stream.Stream;

public class SQSEventWrapper extends JsonPayloadAwsEventWrapper<SQSEvent> {

    public SQSEventWrapper(SQSEvent event) {
        this(event, new ObjectMapper());
    }

    public SQSEventWrapper(SQSEvent event, ObjectMapper objectMapper) {
        super(event, objectMapper);
    }

    @Override
    protected Stream<String> getJsonStream(SQSEvent event) {
        return event.getRecords().stream().map(sqsRecord-> sqsRecord.getBody());
    }
}
