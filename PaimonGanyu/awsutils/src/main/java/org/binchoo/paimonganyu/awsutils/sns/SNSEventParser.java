package org.binchoo.paimonganyu.awsutils.sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.JsonPayloadAwsEventParser;

import java.util.stream.Stream;

public class SNSEventParser extends JsonPayloadAwsEventParser<SNSEvent> {

    public SNSEventParser() {
        super();
    }

    public SNSEventParser(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected Stream<String> getJsonStream(SNSEvent event) {
        return event.getRecords().stream().map(snsRecord-> snsRecord.getSNS().getMessage());
    }
}
