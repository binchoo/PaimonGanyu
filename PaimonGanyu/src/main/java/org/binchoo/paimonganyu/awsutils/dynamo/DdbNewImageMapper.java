package org.binchoo.paimonganyu.awsutils.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import org.binchoo.paimonganyu.awsutils.AwsEventMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This utility class can convert the NewImage JSONs of DynamodbEvent to a list of java POJO.
 * @param <T> the POJO type you use to represent the dynamodb item.
 */
public class DdbNewImageMapper<T> implements AwsEventMapper<DynamodbEvent, T> {

    private static final DdbEventName[] allowedEventNames = {DdbEventName.MODIFY, DdbEventName.INSERT};

    private final DynamodbEvent dynamodbEvent;
    private final DynamoDBMapper dynamoDBMapper;
    private final Class<T> clazz;

    private List<T> pojos;

    /**
     *
     * @param dynamodbEvent the {@link DynamodbEvent} object inject received from the AWS lambda service
     * @param dynamoDBMapper the {@link DynamoDBMapper} to use
     * @param clazz the POJO class you use to represent the dynamodb item.
     */
    public DdbNewImageMapper(DynamodbEvent dynamodbEvent, DynamoDBMapper dynamoDBMapper, Class<T> clazz) {
        this.dynamodbEvent = dynamodbEvent;
        this.dynamoDBMapper = dynamoDBMapper;
        this.clazz = clazz;
        doMapping();
    }

    private void doMapping() {
        this.pojos = dynamodbEvent.getRecords().stream().filter(this::recordEventNameFilter)
                .map(this::recordNewImageGetter)
                .map(DdbNewImagePackageCoverter::fromLambdaToDdb)
                .map(newImage-> dynamoDBMapper.marshallIntoObject(clazz, newImage))
                .collect(Collectors.toList());
    }

    private boolean recordEventNameFilter(DynamodbEvent.DynamodbStreamRecord record) {
        DdbEventName eventName = DdbEventName.valueOf(record.getEventName());
        for (DdbEventName allowedEventName : allowedEventNames) {
            if (eventName.equals(allowedEventName)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, AttributeValue> recordNewImageGetter(DynamodbEvent.DynamodbStreamRecord record) {
        return record.getDynamodb().getNewImage();
    }

    /**
     * Get the list of POJO that hydrate DynamoDBEvent::Records::NewImage
     * @return unmodifiable list of POJO that hydrate DynamoDBEvent::Records::NewImage
     */
    public List<T> getPojos() {
        return Collections.unmodifiableList(this.pojos);
    }
}
