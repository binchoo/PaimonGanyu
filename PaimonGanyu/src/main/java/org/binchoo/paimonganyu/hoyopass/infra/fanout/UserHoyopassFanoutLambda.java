package org.binchoo.paimonganyu.hoyopass.infra.fanout;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.dynamo.DdbNewImageMapper;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.infra.dynamo.item.UserHoyopassItem;

public class UserHoyopassFanoutLambda {

    private static final String USERHOYOPASS_TOPIC = System.getenv("USERHOYOPASS_TOPIC");

    private final DynamoDBMapper dynamodbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

    public void handler(DynamodbEvent dynamodbEvent) {
        new DdbNewImageMapper(dynamodbMapper)
                .extractPojos(dynamodbEvent, UserHoyopassItem.class).stream()
                .map(UserHoyopassItem::toDomain)
                .map(this::createMessage)
                .forEach(this::publish);
    }

    private UserHoyopassMessage createMessage(UserHoyopass userHoyopass) {
        return new UserHoyopassMessage(userHoyopass);
    }

    private void publish(UserHoyopassMessage messsage) {
        try {
            snsClient.publish(USERHOYOPASS_TOPIC, objectMapper.writeValueAsString(messsage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Error occurred while serializing a UserHoyopassMessage: %s", messsage), e);
        }
    }
}
