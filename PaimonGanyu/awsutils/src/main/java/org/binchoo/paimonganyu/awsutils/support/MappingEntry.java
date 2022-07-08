package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventParser;

/**
 * @param <E> The type of lambda event.
 */
public final class MappingEntry<E> {

    private final WrappingManual parent;
    private final Class<E> event;
    private ParserSpec<E, ? extends AwsEventParser<E>> parserSpec;

    MappingEntry(WrappingManual wrappingManual, Class<E> event) {
        this.parent = wrappingManual;
        this.event = event;
        this.parserSpec = null;
    }

    /**
     * A event parser class that will wrap the preceded event class.
     */
    public <W extends AwsEventParser<E>> ParserSpec<E, W> wrapIn(Class<W> parserClass) {
        this.parserSpec = new ParserSpec<>(this, parserClass);
        return (ParserSpec<E, W>) this.parserSpec;
    }

    public WrappingManual and() {
        return this.parent;
    }

    AwsEventParser<E> newParser(Object[] constructorArgs) {
        return this.parserSpec.newParser(constructorArgs);
    }

    ParserSpec<E, AwsEventParser<E>> getWrapperSpec() {
        return (ParserSpec<E, AwsEventParser<E>>) this.parserSpec;
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
                ", parserSpec=" + parserSpec +
                '}';
    }
}
