package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @param <W> The type of event wrapper.
 */
public final class EventWrapperSpec<E, W extends AwsEventWrapper<E>> {

    private final Logger logger = LoggerFactory.getLogger(EventWrapperSpec.class);
    private final MappingEntry<E> parent;
    private final Class<W> eventWrapperClass;

    private Class<?>[] constructorArgTypes = null;
    private boolean requireArgs = false;

    public EventWrapperSpec(MappingEntry<E> mappingEntry, Class<W> eventWrapperClass) {
        this.parent = mappingEntry;
        this.eventWrapperClass = eventWrapperClass;
    }

    /**
     * Argument types of a constructor method that will instantiate the preceded event wrapper.
     */
    public EventWrapperSpec<E, W> argTypes(Class<?>... constructorArgTypes) {
        this.constructorArgTypes = constructorArgTypes;
        this.requireArgs = true;
        return this;
    }

    public AwsEventWrappingManual and() {
        return this.parent.getParent();
    }

    protected AwsEventWrapper<E> createInstance(Object[] constructorArgs) {
        try {
            var constructor = eventWrapperClass.getDeclaredConstructor(this.constructorArgTypes);
            return constructor.newInstance(constructorArgs);
        } catch (InstantiationException | InvocationTargetException e) {
            logger.error("Error instantiating a event wrapper: {}", eventWrapperClass);
        } catch (IllegalAccessException e) {
            logger.error("Could not reach the event wrapper type: {}", eventWrapperClass);
        } catch (NoSuchMethodException e) {
            logger.error("Could not find a matching constructor for: {}", eventWrapperClass);
        }
        return null;
    }

    @Override
    public String toString() {
        return "EventWrapperSpec{" +
                "eventWrapperClass=" + eventWrapperClass +
                ", useAwsClient=" + requireArgs +
                '}';
    }
}