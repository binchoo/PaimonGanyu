package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

import java.util.LinkedList;

/**
 * @param <E> The type of lambda event.
 */
public final class MappingEntry<E> {

    private final AwsEventWrappingManual parent;
    private final LinkedList<EventWrapperSpec<E, ? extends AwsEventWrapper<E>>> wrappersForEvent;

    public MappingEntry(AwsEventWrappingManual awsEventWrappingManual, Class<E> eventClass) {
        this.parent = awsEventWrappingManual;
        this.wrappersForEvent = new LinkedList<>();
    }

    /**
     * A event wrapper class that will wrap the preceded event class.
     */
    public <W extends AwsEventWrapper<E>> EventWrapperSpec<E, W> wrapBy(Class<W> eventWrapperClass) {
        EventWrapperSpec<E, W> eventWrapperSpec = new EventWrapperSpec<>(this, eventWrapperClass);
        this.wrappersForEvent.addFirst(eventWrapperSpec);
        return eventWrapperSpec;
    }

    protected EventWrapperSpec<E, ? extends AwsEventWrapper<E>> getDefaultEventWrapperSpec() {
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