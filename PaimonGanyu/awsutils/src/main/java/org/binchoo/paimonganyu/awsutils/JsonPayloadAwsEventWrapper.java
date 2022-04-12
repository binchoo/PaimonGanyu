package org.binchoo.paimonganyu.awsutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class JsonPayloadAwsEventWrapper<E> implements AwsEventWrapper {

    private final E event;
    private final ObjectMapper objectMapper;

    protected JsonPayloadAwsEventWrapper(E event) {
       this(event, new ObjectMapper());
    }

    protected JsonPayloadAwsEventWrapper(E event, ObjectMapper objectMapper) {
        this.event = event;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> List<T> extractPojos(Class<T> clazz) {
        return Collections.unmodifiableList(
                this.getJsonStream(event)
                        .map(json -> this.deserialize(json, clazz))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    protected abstract Stream<String> getJsonStream(E event);

    private <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.warn(String.format("Failed to deserialize a message: %s", json), e);
        }
        return null;
    }
}
