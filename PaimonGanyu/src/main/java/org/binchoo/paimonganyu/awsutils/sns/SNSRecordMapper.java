package org.binchoo.paimonganyu.awsutils.sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.AwsEventMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SNSRecordMapper implements AwsEventMapper<SNSEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> List<T> extractPojos(SNSEvent event, Class<T> clazz) {
        return Collections.unmodifiableList(this.doMapping(event, clazz));
    }

    private <T> List<T> doMapping(SNSEvent event, Class<T> clazz) {
        return event.getRecords().stream()
                .map(this::snsMessageGetter)
                .map(msg-> this.messageDeserializer(msg, clazz))
                .collect(Collectors.toList());
    }

    private String snsMessageGetter(SNSEvent.SNSRecord record) {
        return record.getSNS().getMessage();
    }

    private <T> T messageDeserializer(String message, Class<T> clazz) {
        try {
            return objectMapper.readValue(message, clazz);
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Failed to deserialize a SNS message: ", message), e);
        }
    }
}
