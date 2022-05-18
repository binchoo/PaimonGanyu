package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

/**
 * @param <E> The type of lambda event.
 */
public final class MappingEntry<E> {

    private final WrappingManual parent;
    private final Class<E> event;
    private WrapperSpec<E, ? extends AwsEventWrapper<E>> wrapperSpec;

    protected MappingEntry(WrappingManual wrappingManual, Class<E> event) {
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

    protected AwsEventWrapper<E> createWrapper(Object[] constructorArgs) {
        return this.wrapperSpec.createInstance(constructorArgs);
    }

    protected WrapperSpec<E, ? extends AwsEventWrapper<E>> getWrapperSpec() {
        return this.wrapperSpec;
    }

    protected WrappingManual getParent() {
        return this.parent;
    }

    protected Class<?> getEvent() {
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
