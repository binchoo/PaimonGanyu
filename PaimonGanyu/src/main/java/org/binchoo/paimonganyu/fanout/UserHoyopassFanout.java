package org.binchoo.paimonganyu.fanout;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.hoyopass.infra.dynamo.item.UserHoyopassItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserHoyopassFanout {

    private static final String USERHOYOPASS_TOPIC = System.getenv("USERHOYOPASS_TOPIC");
    private static final String INSERT = "INSERT";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

    public void fanout(DynamodbEvent ddbStream) throws JsonProcessingException {
        List<UserHoyopassItem> hoyopasses = getNewUserHoyopasses(ddbStream);
        hoyopasses.forEach(hoyopass-> {
            try {
                System.out.println(hoyopass);
                snsClient.publish(USERHOYOPASS_TOPIC, objectMapper.writeValueAsString(hoyopass));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private List<UserHoyopassItem> getNewUserHoyopasses(DynamodbEvent ddbStream) throws JsonProcessingException {
        return ddbStream.getRecords().stream()
                .filter(record-> record.getEventName().equals(INSERT))
                .map(record -> record.getDynamodb().getNewImage())
                .map(this::fromLambdaToDynamodb)
                .filter(Objects::nonNull)
                .map(newImage -> dynamoDBMapper.marshallIntoObject(UserHoyopassItem.class, newImage))
                .collect(Collectors.toList());
    }

    private Map<String, AttributeValue> fromLambdaToDynamodb(
            Map<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue> attributeValue) {

        try {
            String json = objectMapper.writeValueAsString(attributeValue);
            return objectMapper.readValue(json, new TypeReference<Map<String, AttributeValue>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
