package org.binchoo.paimonganyu.awsutils.dynamo;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

class AttributeValuePackageConversion {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private AttributeValuePackageConversion() {}

    protected static Map<String, AttributeValue> fromLambdaToDdb(
            Map<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue> item) {
        try {
            String json = objectMapper.writeValueAsString(item);
            return objectMapper.readValue(json, new TypeReference<Map<String, AttributeValue>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    String.format("DynamoDB item has unknown structure: %s", item.toString()), e);
        }
    }
}
