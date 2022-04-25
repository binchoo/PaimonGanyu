package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

public final class EventWrapperSpec {

    private final MappingEntry parent;
    private final Class<? extends AwsEventWrapper<?>> eventWrapperClass;

    private Class<?>[] constructorArgs = null;
    private boolean requireArgs = false;

    public EventWrapperSpec(MappingEntry mappingEntry, Class<? extends AwsEventWrapper<?>> eventWrapperClass) {
        this.parent = mappingEntry;
        this.eventWrapperClass = eventWrapperClass;
    }

    /**
     * Argument types of a constructor method that will instantiate the preceded event wrapper.
     */
    public EventWrapperSpec argTypes(Class<?>... constructorArgs) {
        this.constructorArgs = constructorArgs;
        this.requireArgs = true;
        return this;
    }

    public AwsEventWrappingManual and() {
        return this.parent.getParent();
    }

    protected Class<? extends AwsEventWrapper<?>> getEventWrapperClass() {
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