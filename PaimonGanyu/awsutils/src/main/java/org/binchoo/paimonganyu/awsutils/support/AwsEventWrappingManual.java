package org.binchoo.paimonganyu.awsutils.support;


import java.util.HashMap;
import java.util.Map;

public final class AwsEventWrappingManual {

    private final Map<Class<?>, MappingEntry<?>> eventEntryMap = new HashMap<>();

    /**
     * Start to configure the event wrapper mapping for this event class.
     */
    public <E> MappingEntry<E> whenEvent(Class<E> eventClass) {
        MappingEntry<E> mappingEntry = new MappingEntry<>(this, eventClass);
        this.eventEntryMap.put(eventClass, mappingEntry);
        return mappingEntry;
    }

    protected boolean contains(Class<?> eventClass) {
        return this.eventEntryMap.containsKey(eventClass);
    }

    protected MappingEntry<?> getMappingEntry(Class<?> eventClass) {
        if (this.contains(eventClass)) {
            return this.eventEntryMap.get(eventClass);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Could not find a event wrapper specification for event type %s", eventClass));
        }
    }

    @Override
    public String toString() {
        return "AwsEventWrapperManual{" +
                "eventEntryMap=" + eventEntryMap +
                '}';
    }
}
