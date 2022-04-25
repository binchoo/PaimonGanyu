package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

/**
 * @param <W> The type of event wrapper.
 */
public final class EventWrapperSpec<E, W extends AwsEventWrapper<E>> {

    private final MappingEntry<E> parent;
    private final Class<W> eventWrapperClass;

    private Class<?>[] constructorArgs = null;
    private boolean requireArgs = false;

    public EventWrapperSpec(MappingEntry<E> mappingEntry, Class<W> eventWrapperClass) {
        this.parent = mappingEntry;
        this.eventWrapperClass = eventWrapperClass;
    }

    /**
     * Argument types of a constructor method that will instantiate the preceded event wrapper.
     */
    public EventWrapperSpec<E, W> argTypes(Class<?>... constructorArgs) {
        this.constructorArgs = constructorArgs;
        this.requireArgs = true;
        return this;
    }

    public AwsEventWrappingManual and() {
        return this.parent.getParent();
    }

    protected Class<W> getEventWrapperClass() {
        return this.eventWrapperClass;
    }

    protected boolean getRequireArgs() {
        return this.requireArgs;
    }

    protected Class<?>[] getConstructorArgs() {
        return this.constructorArgs;
    }

    @Override
    public String toString() {
        return "EventWrapperSpec{" +
                "eventWrapperClass=" + eventWrapperClass +
                ", useAwsClient=" + requireArgs +
                '}';
    }
}