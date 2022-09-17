package org.binchoo.paimonganyu.awsutils;

import java.util.List;

public interface AwsEventParser<E> {

    /**
     * Get the list of POJO type of {@link T} that hydrate {@link E}::Records::*
     * @param clazz the pojo type's Class object
     * @return unmodifiable list of POJO
     */
    <T> List<T> extractPojos(E event, Class<T> clazz);
}
