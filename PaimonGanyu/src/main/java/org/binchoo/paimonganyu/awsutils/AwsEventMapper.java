package org.binchoo.paimonganyu.awsutils;

import java.util.List;

/**
 * @param <E> The Event type. This should be one of event types in com.amazonaws:aws-lambda-java-events module.
 */
public interface AwsEventMapper<E> {

    /**
     * Get the list of POJO type of {@link T} that hydrate {@link E}::Records::*
     * @param event an aws lambda event
     * @param clazz the pojo type's Class object
     * @param <T> the pojo type
     * @return unmodifiable list of POJO
     */
    <T> List<T> extractPojos(E event, Class<T> clazz);
}
