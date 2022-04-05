package org.binchoo.paimonganyu.awsutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class JsonPayloadAwsEventWrapper<E> implements AwsEventWrapper<E> {

    private static final Logger logger = LoggerFactory.getLogger(JsonPayloadAwsEventWrapper.class);

    private final E event;
    private final ObjectMapper objectMapper;

    public JsonPayloadAwsEventWrapper(E event) {
       this(event, new ObjectMapper());
    }

    public JsonPayloadAwsEventWrapper(E event, ObjectMapper objectMapper) {
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
        } catch (JsonProcessingException e) {
            logger.warn(String.format("Failed to deserialize a message: %s", json));
            e.printStackTrace();
        }
        return null;
    }
}
