package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

/**
 * @param <E> The type of lambda event.
 */
public final class MappingEntry<E> {

    private final WrappingManual parent;
    private final Class<E> event;
    private WrapperSpec<E, ? extends AwsEventWrapper<E>> wrapperSpec;

    MappingEntry(WrappingManual wrappingManual, Class<E> event) {
        this.parent = wrappingManual;
        this.event = event;
        this.wrapperSpec = null;
    }

    /**
     * A event wrapper class that will wrap the preceded event class.
     */
    public <W extends AwsEventWrapper<E>> WrapperSpec<E, W> wrapIn(Class<W> wrapperClass) {
        this.wrapperSpec = new WrapperSpec<>(this, wrapperClass);
        return (WrapperSpec<E, W>) this.wrapperSpec;
    }

    public WrappingManual and() {
        return this.parent;
    }

    AwsEventWrapper<E> newWrapper(Object[] constructorArgs) {
        return this.wrapperSpec.newWrapper(constructorArgs);
    }

    WrapperSpec<E, AwsEventWrapper<E>> getWrapperSpec() {
        return (WrapperSpec<E, AwsEventWrapper<E>>) this.wrapperSpec;
    }

    WrappingManual getParent() {
        return this.parent;
    }

    Class<?> getEvent() {
        return this.event;
    }

    @Override
    public String toString() {
        return "MappingEntry{" +
                ", event=" + event +
                ", wrapperSpec=" + wrapperSpec +
                '}';
    }
}
