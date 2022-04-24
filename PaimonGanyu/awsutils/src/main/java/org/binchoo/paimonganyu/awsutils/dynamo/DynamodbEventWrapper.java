package org.binchoo.paimonganyu.awsutils.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This utility class can convert the NewImage JSONs of DynamodbEvent to a list of java POJO.
 */
public class DynamodbEventWrapper implements AwsEventWrapper<DynamodbEvent> {

    private static final DynamodbEventName[] defaultAllowedEventNames
            = {DynamodbEventName.MODIFY, DynamodbEventName.INSERT};

    private final DynamoDBMapper dynamoDBMapper;
    private final DynamodbEventName[] allowedEventNames;

    /**
     * @param dynamoDBMapper the {@link DynamoDBMapper} to use
     */
    public DynamodbEventWrapper(DynamoDBMapper dynamoDBMapper) {
        this(dynamoDBMapper, defaultAllowedEventNames);
    }

    public DynamodbEventWrapper(DynamoDBMapper dynamoDBMapper, DynamodbEventName... allowedEventNames) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.allowedEventNames = allowedEventNames;
    }

    /**
     * Get the list of POJO that hydrate DynamoDBEvent::Records::NewImage
     * @return unmodifiable list of POJO that hydrate DynamoDBEvent::Records::NewImage
     */
    @Override
    public <T> List<T> extractPojos(DynamodbEvent event, Class<T> clazz) {
        return Collections.unmodifiableList(this.doMapping(event, clazz));
    }

    private <T> List<T> doMapping(DynamodbEvent event, Class<T> clazz) {
        return event.getRecords().stream().filter(this::recordEventNameFilter)
                .map(this::recordNewImageGetter)
                .map(AttributeValuePackageConversion::fromLambdaToDdb)
                .map(newImage-> dynamoDBMapper.marshallIntoObject(clazz, newImage))
                .collect(Collectors.toList());
    }

    private boolean recordEventNameFilter(DynamodbEvent.DynamodbStreamRecord streamRecord) {
        DynamodbEventName eventName = DynamodbEventName.valueOf(streamRecord.getEventName());
        for (DynamodbEventName allowedEventName : allowedEventNames) {
            if (eventName.equals(allowedEventName)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, AttributeValue> recordNewImageGetter(DynamodbEvent.DynamodbStreamRecord streamRecord) {
        return streamRecord.getDynamodb().getNewImage();
    }
}
