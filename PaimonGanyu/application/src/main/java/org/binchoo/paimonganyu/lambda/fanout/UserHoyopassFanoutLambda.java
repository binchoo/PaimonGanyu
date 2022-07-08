package org.binchoo.paimonganyu.lambda.fanout;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.binchoo.paimonganyu.awsutils.support.template.AsyncEventWrappingLambda;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.item.UserHoyopassItem;
import org.binchoo.paimonganyu.lambda.dailycheck.dto.UserHoyopassMessage;

/**
 * Fanout lambda should start fast, so do not use spring context.
 */
public class UserHoyopassFanoutLambda extends AsyncEventWrappingLambda<DynamodbEvent> {

    private static final String USERHOYOPASS_TOPIC = System.getenv("USERHOYOPASS_TOPIC");

    private final DynamoDBMapper dynamodbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

    @Override
    protected void lookupDependencies() {
        // inline-dependencies declared.
    }

    @Override
    protected Object[] getConstructorArgs() {
        return new Object[] { dynamodbMapper };
    }

    @Override
    protected void doHandle(DynamodbEvent event, AwsEventParser<DynamodbEvent> eventParser) {
        eventParser.extractPojos(event, UserHoyopassItem.class).stream()
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
            throw new IllegalArgumentException(
                    String.format("Error occurred while serializing a UserHoyopassMessage: %s", messsage), e);
        }
    }
}
