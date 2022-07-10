package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @param <W> The type of event parser.
 */
public final class ParserSpec<E, W extends AwsEventParser<E>> {

    private static final Logger logger = LoggerFactory.getLogger(ParserSpec.class);

    private final MappingEntry<E> parent;
    private final Class<W> parser;

    private Class<?>[] constructorArgTypes;

    ParserSpec(MappingEntry<E> mappingEntry, Class<W> parser) {
        this.parent = mappingEntry;
        this.parser = parser;
        this.constructorArgTypes = null;
    }

    /**
     * Argument types of a constructor method that will instantiate the preceded event parser.
     */
    public ParserSpec<E, W> argTypes(Class<?>... constructorArgTypes) {
        this.constructorArgTypes = constructorArgTypes;
        return this;
    }

    public ParsingManual and() {
        return this.parent.getParent();
    }

    W newParser(Object[] constructorArgs) {
        try {
            var constructor = parser.getDeclaredConstructor(this.constructorArgTypes);
            return constructor.newInstance(constructorArgs);
        } catch (InstantiationException | InvocationTargetException e) {
            logger.error("Error instantiating a event parser: {}", parser);
        } catch (IllegalAccessException e) {
            logger.error("Could not reach the event parser type: {}", parser);
        } catch (NoSuchMethodException e) {
            logger.error("Could not find a matching constructor for: {}", parser);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ParserSpec{" +
                "parent=" + parent +
                ", parser=" + parser +
                ", constructorArgTypes=" + Arrays.toString(constructorArgTypes) +
                '}';
    }
}