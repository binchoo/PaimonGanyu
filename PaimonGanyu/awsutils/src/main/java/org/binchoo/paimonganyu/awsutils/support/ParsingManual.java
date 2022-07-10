package org.binchoo.paimonganyu.awsutils.support;


public final class ParsingManual {

    private final MappingEntries mappingEntries;

    public ParsingManual() {
        this.mappingEntries = new MappingEntries();
    }

    /**
     * Start to configure the event wrapper mapping for this event class.
     */
    public <E> MappingEntry<E> whenEvent(Class<E> eventClass) {
        MappingEntry<E> mappingEntry = new MappingEntry<>(this, eventClass);
        this.mappingEntries.add(mappingEntry);
        return mappingEntry;
    }

    protected boolean contains(Class<?> eventClass) {
        return this.mappingEntries.contains(eventClass);
    }

    protected <E> MappingEntry<E> getMappingEntry(Class<E> eventClass) {
        return this.mappingEntries.findMapping(eventClass);
    }

    @Override
    public String toString() {
        return "ParsingManual{" +
                "mappingEntries=" + mappingEntries +
                '}';
    }
}
