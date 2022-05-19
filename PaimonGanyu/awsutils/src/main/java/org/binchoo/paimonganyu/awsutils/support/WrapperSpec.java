package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @param <W> The type of event wrapper.
 */
public final class WrapperSpec<E, W extends AwsEventWrapper<E>> {

    private static final Logger logger = LoggerFactory.getLogger(WrapperSpec.class);

    private final MappingEntry<E> parent;
    private final Class<W> wrapper;

    private Class<?>[] constructorArgTypes;

    WrapperSpec(MappingEntry<E> mappingEntry, Class<W> wrapper) {
        this.parent = mappingEntry;
        this.wrapper = wrapper;
        this.constructorArgTypes = null;
    }

    /**
     * Argument types of a constructor method that will instantiate the preceded event wrapper.
     */
    public WrapperSpec<E, W> argTypes(Class<?>... constructorArgTypes) {
        this.constructorArgTypes = constructorArgTypes;
        return this;
    }

    public WrappingManual and() {
        return this.parent.getParent();
    }

    W createInstance(Object[] constructorArgs) {
        try {
            var constructor = wrapper.getDeclaredConstructor(this.constructorArgTypes);
            return constructor.newInstance(constructorArgs);
        } catch (InstantiationException | InvocationTargetException e) {
            logger.error("Error instantiating a event wrapper: {}", wrapper);
        } catch (IllegalAccessException e) {
            logger.error("Could not reach the event wrapper type: {}", wrapper);
        } catch (NoSuchMethodException e) {
            logger.error("Could not find a matching constructor for: {}", wrapper);
        }
        return null;
    }

    @Override
    public String toString() {
        return "WrapperSpec{" +
                "parent=" + parent +
                ", wrapper=" + wrapper +
                ", constructorArgTypes=" + Arrays.toString(constructorArgTypes) +
                '}';
    }
}