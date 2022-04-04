package org.binchoo.paimonganyu.aws;

import java.util.List;

/**
 * @param <E> The Event type in com.amazonaws:aws-lambda-java-events
 * @param <T> The POJO type that hydrate records in the aws event.
 */
public interface AwsEventMapper<E, T> {

    /**
     * Get the list of POJO type of {@link T} that hydrate {@link E}::Records::*
     * @return unmodifiable list of POJO
     */
    List<T> getPojos();
}
