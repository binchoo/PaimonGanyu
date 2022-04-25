package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

import java.util.LinkedList;

public final class MappingEntry {

    private final AwsEventWrappingManual parent;
    private final LinkedList<EventWrapperSpec> wrappersForEvent;

    public MappingEntry(AwsEventWrappingManual awsEventWrappingManual, Class<?> eventClass) {
        this.parent = awsEventWrappingManual;
        this.wrappersForEvent = new LinkedList<>();
    }

    /**
     * A event wrapper class that will wrap the preceded event class.
     */
    public EventWrapperSpec wrapBy(Class<? extends AwsEventWrapper<?>> eventWrapperClass) {
        EventWrapperSpec eventWrapperSpec = new EventWrapperSpec(this, eventWrapperClass);
        this.wrappersForEvent.addFirst(eventWrapperSpec);
        return eventWrapperSpec;
    }

    protected EventWrapperSpec getDefaultEventWrapperSpec() {
        return this.wrappersForEvent.getFirst();
    }

    protected AwsEventWrappingManual getParent() {
        return this.parent;
    }

    @Override
    public String toString() {
        return "EventEntry{" +
                "wrappersForEvent=" + wrappersForEvent +
                '}';
    }
}