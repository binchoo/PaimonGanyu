package org.binchoo.paimonganyu.awsutils.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-04-26
 */
final class MappingEntries {

    private final Map<Class<?>, MappingEntry<?>> mappingEntryMap;

    protected MappingEntries() {
        this.mappingEntryMap = new HashMap<>();
    }

    protected <E> void add(MappingEntry<E> mappingEntry) {
        this.mappingEntryMap.put(mappingEntry.getEvent(), mappingEntry);
    }

    protected boolean contains(Class<?> eventClass) {
        return this.mappingEntryMap.containsKey(eventClass);
    }

    protected <E> MappingEntry<E> findMapping(Class<E> eventClass) {
        if (this.contains(eventClass))
            return (MappingEntry<E>) this.mappingEntryMap.get(eventClass);
        else
            throw new IllegalArgumentException(String.format(
                "Could not find a mapping entry for event type %s", eventClass));
    }

    @Override
    public String toString() {
        return "MappingEntries{" +
                "mappingEntryMap=" + mappingEntryMap +
                '}';
    }
}
