package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;

public final class EventWrapperSpec {

    private final MappingEntry parent;
    private final Class<? extends AwsEventWrapper<?>> eventWrapperClass;

    private Class<?> clientClass = null;
    private boolean useAwsClient = false;

    public EventWrapperSpec(MappingEntry mappingEntry, Class<? extends AwsEventWrapper<?>> eventWrapperClass) {
        this.parent = mappingEntry;
        this.eventWrapperClass = eventWrapperClass;
    }

    public EventWrapperSpec useAwsClient(Class<?> clientClass) {
        this.clientClass = clientClass;
        this.useAwsClient = true;
        return this;
    }

    public AwsEventWrappingManual and() {
        return this.parent.getParent();
    }

    protected Class<? extends AwsEventWrapper<?>> getEventWrapperClass() {
        return this.eventWrapperClass;
    }

    protected boolean getUseAwsClient() {
        return this.useAwsClient;
    }

    protected Class<?> getClientClass() {
        return this.clientClass;
    }

    @Override
    public String toString() {
        return "EventWrapperSpec{" +
                "eventWrapperClass=" + eventWrapperClass +
                ", useAwsClient=" + useAwsClient +
                '}';
    }
}