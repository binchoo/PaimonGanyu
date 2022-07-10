package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventParser;

/**
 * @param <E> The type of lambda event.
 */
public final class MappingEntry<E> {

    private final ParsingManual parent;
    private final Class<E> event;
    private ParserSpec<E, ? extends AwsEventParser<E>> parserSpec;

    MappingEntry(ParsingManual parsingManual, Class<E> event) {
        this.parent = parsingManual;
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

    public ParsingManual and() {
        return this.parent;
    }

    AwsEventParser<E> newParser(Object[] constructorArgs) {
        return this.parserSpec.newParser(constructorArgs);
    }

    ParserSpec<E, AwsEventParser<E>> getParserSpec() {
        return (ParserSpec<E, AwsEventParser<E>>) this.parserSpec;
    }

    ParsingManual getParent() {
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
