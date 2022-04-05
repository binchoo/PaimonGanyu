package org.binchoo.paimonganyu.awsutils.sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.JsonPayloadAwsEventWrapper;

import java.util.stream.Stream;

public class SNSEventWrapper extends JsonPayloadAwsEventWrapper<SNSEvent> {

    public SNSEventWrapper(SNSEvent event) {
        this(event, new ObjectMapper());
    }

    public SNSEventWrapper(SNSEvent event, ObjectMapper objectMapper) {
        super(event, objectMapper);
    }

    @Override
    protected Stream<String> getJsonStream(SNSEvent event) {
        return event.getRecords().stream().map(snsRecord-> snsRecord.getSNS().getMessage());
    }
}
