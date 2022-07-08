package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @param <P> The type of event parser.
 */
public final class ParserSpec<E, P extends AwsEventParser<E>> {

    private static final Logger logger = LoggerFactory.getLogger(ParserSpec.class);

    private final MappingEntry<E> parent;
    private final Class<P> parserClass;

    private Class<?>[] constructorArgTypes;

    ParserSpec(MappingEntry<E> mappingEntry, Class<P> parserClass) {
        this.parent = mappingEntry;
        this.parserClass = parserClass;
        this.constructorArgTypes = null;
    }

    /**
     * Argument types of a constructor method that will instantiate the preceded event parser.
     */
    public ParserSpec<E, P> argTypes(Class<?>... constructorArgTypes) {
        this.constructorArgTypes = constructorArgTypes;
        return this;
    }

    public WrappingManual and() {
        return this.parent.getParent();
    }

    P newParser(Object[] constructorArgs) {
        try {
            var constructor = parserClass.getDeclaredConstructor(this.constructorArgTypes);
            return constructor.newInstance(constructorArgs);
        } catch (InstantiationException | InvocationTargetException e) {
            logger.error("Error instantiating a event parser: {}", parserClass);
        } catch (IllegalAccessException e) {
            logger.error("Could not reach the event parser type: {}", parserClass);
        } catch (NoSuchMethodException e) {
            logger.error("Could not find a matching constructor for: {}", parserClass);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ParserSpec{" +
                "parent=" + parent +
                ", parserClass=" + parserClass +
                ", constructorArgTypes=" + Arrays.toString(constructorArgTypes) +
                '}';
    }
}