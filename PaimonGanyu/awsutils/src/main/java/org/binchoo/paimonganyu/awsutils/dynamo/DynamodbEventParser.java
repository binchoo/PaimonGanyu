package org.binchoo.paimonganyu.awsutils.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import org.binchoo.paimonganyu.awsutils.AwsEventParser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This utility class can convert the NewImage JSONs of DynamodbEvent to a list of java POJO.
 */
public class DynamodbEventParser implements AwsEventParser<DynamodbEvent> {

    private static final EnumSet<DynamodbEventName> defaultAllowedEventNames
            = EnumSet.of(DynamodbEventName.MODIFY, DynamodbEventName.INSERT);

    private final DynamoDBMapper dynamoDBMapper;
    private final EnumSet<DynamodbEventName> allowedEventNames;

    /**
     * @param dynamoDBMapper the {@link DynamoDBMapper} to use
     */
    public DynamodbEventParser(DynamoDBMapper dynamoDBMapper) {
        this(dynamoDBMapper, defaultAllowedEventNames);
    }

    public DynamodbEventParser(DynamoDBMapper dynamoDBMapper, DynamodbEventName... allowedEventNames) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.allowedEventNames = EnumSet.noneOf(DynamodbEventName.class);
        this.allowedEventNames.addAll(Arrays.asList(allowedEventNames));
    }

    public DynamodbEventParser(DynamoDBMapper dynamoDBMapper, EnumSet<DynamodbEventName> defaultAllowedEventNames) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.allowedEventNames = defaultAllowedEventNames;
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
                .map(this::fromRecordToNewImage)
                .map(AttributeValuePackageConversion::fromLambdaToDdb)
                .map(newImage-> dynamoDBMapper.marshallIntoObject(clazz, newImage))
                .collect(Collectors.toList());
    }

    private boolean recordEventNameFilter(DynamodbEvent.DynamodbStreamRecord streamRecord) {
        DynamodbEventName eventName = DynamodbEventName.valueOf(streamRecord.getEventName());
        for (DynamodbEventName allowedEventName : allowedEventNames)
            if (eventName.equals(allowedEventName))
                return true;
        return false;
    }

    private Map<String, AttributeValue> fromRecordToNewImage(DynamodbEvent.DynamodbStreamRecord streamRecord) {
        return streamRecord.getDynamodb().getNewImage();
    }
}
