package org.binchoo.paimonganyu.awsutils;

import java.util.List;

/**
 * @param <E> The Event type. This should be one of event types in com.amazonaws:aws-lambda-java-events module.
 * @param <T> The POJO type that hydrate records in the aws event.
 */
public interface AwsEventMapper<E, T> {

    /**
     * Get the list of POJO type of {@link T} that hydrate {@link E}::Records::*
     * @return unmodifiable list of POJO
     */
    List<T> getPojos();
}
