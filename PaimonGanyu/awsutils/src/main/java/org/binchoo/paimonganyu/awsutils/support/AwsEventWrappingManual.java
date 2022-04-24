package org.binchoo.paimonganyu.awsutils.support;

import java.util.HashMap;
import java.util.Map;

public final class AwsEventWrappingManual {

    private final Map<Class<?>, MappingEntry> eventEntryMap = new HashMap<>();

    public MappingEntry whenEvent(Class<?> eventClass) {
        MappingEntry mappingEntry = new MappingEntry(this, eventClass);
        this.eventEntryMap.put(eventClass, mappingEntry);
        return mappingEntry;
    }

    public AwsEventWrappingManual and() {
        return this;
    }

    protected boolean contains(Class<?> eventClass) {
        return this.eventEntryMap.containsKey(eventClass);
    }

    protected EventWrapperSpec getEventWrapperSpec(Class<?> eventClass) {
        if (this.contains(eventClass)) {
            MappingEntry mappingEntry = this.eventEntryMap.get(eventClass);
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
