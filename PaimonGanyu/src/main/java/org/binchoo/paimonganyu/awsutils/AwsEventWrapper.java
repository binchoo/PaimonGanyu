package org.binchoo.paimonganyu.awsutils;

import java.util.List;

/**
 * @param <E> The Event type. This should be one of event types in com.amazonaws:aws-lambda-java-events module.
 */
public interface AwsEventWrapper<E> {

    /**
     * Get the list of POJO type of {@link T} that hydrate {@link E}::Records::*
     * @param clazz the pojo type's Class object
     * @return unmodifiable list of POJO
     */
    <T> List<T> extractPojos(Class<T> clazz);
}
