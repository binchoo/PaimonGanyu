package org.binchoo.paimonganyu.awsutils.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This utility class can convert the NewImage JSONs of DynamodbEvent to a list of java POJO.
 */
public class DynamodbEventWrapper implements AwsEventWrapper<DynamodbEvent> {

    private static final EnumSet<DynamodbEventName> DEFAULT_ALLOWED_DDB_EVENTS
            = EnumSet.of(DynamodbEventName.MODIFY, DynamodbEventName.INSERT);

    private final DynamoDBMapper dynamoDBMapper;
    private final EnumSet<DynamodbEventName> allowedDDBEvents;

    /**
     * @param dynamoDBMapper the {@link DynamoDBMapper} to use
     */
    public DynamodbEventWrapper(DynamoDBMapper dynamoDBMapper) {
        this(dynamoDBMapper, DEFAULT_ALLOWED_DDB_EVENTS);
    }

    public DynamodbEventWrapper(DynamoDBMapper dynamoDBMapper, DynamodbEventName... allowedDDBEvents) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.allowedDDBEvents = EnumSet.noneOf(DynamodbEventName.class);
        this.allowedDDBEvents.addAll(Arrays.asList(allowedDDBEvents));
    }

    public DynamodbEventWrapper(DynamoDBMapper dynamoDBMapper, EnumSet<DynamodbEventName> DEFAULT_ALLOWED_DDB_EVENTS) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.allowedDDBEvents = DEFAULT_ALLOWED_DDB_EVENTS.clone();
    }

    /**
     * Get the list of POJO that hydrate DynamoDBEvent::Records::NewImage
     * @return unmodifiable list of POJO that hydrate DynamoDBEvent::Records::NewImage
     */
    @Override
    public <T> List<T> extractPojos(DynamodbEvent event, Class<T> clazz) {
        return this.doMapping(event, clazz);
    }

    private <T> List<T> doMapping(DynamodbEvent event, Class<T> clazz) {
        return event.getRecords().stream().filter(this::recordEventNameFilter)
                .map(this::fromRecordToNewImage)
                .map(AttributeValuePackageConversion::fromLambdaToDdb)
                .map(newImage-> dynamoDBMapper.marshallIntoObject(clazz, newImage))
                .collect(Collectors.toUnmodifiableList());
    }

    private boolean recordEventNameFilter(DynamodbEvent.DynamodbStreamRecord streamRecord) {
        DynamodbEventName eventName = DynamodbEventName.valueOf(streamRecord.getEventName());
        for (DynamodbEventName allowedEventName : allowedDDBEvents)
            if (eventName.equals(allowedEventName))
                return true;
        return false;
    }

    private Map<String, AttributeValue> fromRecordToNewImage(DynamodbEvent.DynamodbStreamRecord streamRecord) {
        return streamRecord.getDynamodb().getNewImage();
    }
}