package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

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

    protected <E> EventWrapperSpec<E, ? extends AwsEventWrapper<E>> getEventWrapperSpec(Class<E> eventClass) {
        if (this.contains(eventClass)) {
            MappingEntry<E> mappingEntry = (MappingEntry<E>) this.eventEntryMap.get(eventClass);
            return mappingEntry.getDefaultEventWrapperSpec();
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
