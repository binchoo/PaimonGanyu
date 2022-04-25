package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

import java.util.Arrays;

/**
 * @param <E> The type of lambda event.
 */
public final class MappingEntry<E> {

    private final AwsEventWrappingManual parent;
    private final Class<E> eventClass;
    private EventWrapperSpec<E, ? extends AwsEventWrapper<E>> eventWrapperSpec;

    public MappingEntry(AwsEventWrappingManual awsEventWrappingManual, Class<E> eventClass) {
        this.parent = awsEventWrappingManual;
        this.eventClass = eventClass;
        this.eventWrapperSpec = null;
    }

    /**
     * A event wrapper class that will wrap the preceded event class.
     */
    public <W extends AwsEventWrapper<E>> EventWrapperSpec<E, W> wrapBy(Class<W> wrapperClass) {
        this.eventWrapperSpec = new EventWrapperSpec<>(this, wrapperClass);
        return (EventWrapperSpec<E, W>) this.eventWrapperSpec;
    }

    public AwsEventWrappingManual and() {
        return this.parent;
    }

    protected Class<? extends AwsEventWrapper<E>> getWrapperClass() {
        return this.eventWrapperSpec.getEventWrapperClass();
    }

    protected Class<?>[] getConstructorArgTypes() {
        return this.eventWrapperSpec.getConstructorArgs();
    }

    protected AwsEventWrappingManual getParent() {
        return this.parent;
    }

    @Override
    public String toString() {
        return "MappingEntry{" +
                "eventClass=" + eventClass +
                ", eventWrapperSpec=" + eventWrapperSpec +
                '}';
    }
}
