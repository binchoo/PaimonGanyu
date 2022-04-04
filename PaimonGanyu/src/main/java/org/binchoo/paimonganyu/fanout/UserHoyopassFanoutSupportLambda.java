package org.binchoo.paimonganyu.fanout;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.fanout.mapper.DdbNewImageMapper;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.infra.dynamo.item.UserHoyopassItem;

@Slf4j
public class UserHoyopassFanoutSupportLambda {

    private static final String USERHOYOPASS_TOPIC = System.getenv("USERHOYOPASS_TOPIC");

    private final DynamoDBMapper dynamodbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

    public void handler(DynamodbEvent dynamodbEvent) {
        new DdbNewImageMapper<>(dynamodbEvent, dynamodbMapper, UserHoyopassItem.class)
                .getPojos().stream().map(UserHoyopassItem::toDomain).forEach(this::publish);
    }

    private void publish(UserHoyopass userHoyopass) {
        try {
            snsClient.publish(USERHOYOPASS_TOPIC, objectMapper.writeValueAsString(userHoyopass));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Error occurred while serializing a UserHoyopass: %s", userHoyopass), e);
        }
    }
}
