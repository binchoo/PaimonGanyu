package org.binchoo.paimonganyu.awsutils.support;

import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <p> {@link AwsEventWrapper} 구현체에 대한 팩토리 클래스입니다. 팩토리가 이벤트 래퍼를 생성하기 위해서
 * {@code AWS 이벤트 대 이벤트 래퍼} 매핑을 정의하는 {@link AwsEventWrappingManual} 객체가 필요합니다.
 * <p> 하지만 이미 클래스 수준에서 기본 매핑이 정의되어 있습니다!
 * <p> 매핑을 커스터마이징 하려면 {@link AwsEventWrapperMappingConfigurer}를 사용합니다. 별도의 매핑 설정을 갖는
 * 새로운 팩토리 객체를 만들 수 있습니다.
 * {@link AwsEventWrapper} 구현체는 동적 생성이 가능하도록, 접근 가능한 하나 이상의 생성자를 갖고 있어야 합니다.
 * @author : jbinchoo
 * @since : 2022-04-24
 */
public class AwsEventWrapperFactory {

    private static final String AWS_LAMBDA_EVENTS_PACKAGE = "com.amazonaws.services.lambda.runtime.events";
    private static final Logger logger = LoggerFactory.getLogger(AwsEventWrapperFactory.class);
    private static final AwsEventWrapperFactory defaultInstance;

    private final AwsEventWrappingManual eventWrappingManual = new AwsEventWrappingManual();

    static {
        defaultInstance = AwsEventWrapperFactory.create(new DefaultMappingConfigurer());
    }

    protected AwsEventWrapperFactory() { }

    /**
     * Create a custom {@link AwsEventWrapperFactory} instance
     * whose mapping behavior is customized by the given configurer.
     * @param configurer The configurer that customizes {@code event to event wrapper} mappings.
     */
    public static AwsEventWrapperFactory create(AwsEventWrapperMappingConfigurer configurer) {
        if (configurer == null) {
            return defaultInstance;
        }
        AwsEventWrapperFactory factory = new AwsEventWrapperFactory();
        configurer.configure(factory.eventWrappingManual);
        return factory;
    }

    /**
     * Get an event wrapper that can handle the given event. This is a static method
     * that refers {@code defaultInstance} of {@link AwsEventWrapperFactory}, which means,
     * it uses the default, class-level-defined event wrapper mapping.
     * <p> A basic use-case:
     * <pre>
     * SQSEvent event = ...;
     * AwsEventWrapper&lt;SQSEvent&gt; eventWrapper = AwsEventWrapper.getWrapper(event);
     * List&lt;Message&gt; messages = eventWrapper.extractPojos(Message.class);
     * </pre>
     * @param event One of AWS lambda events.
     * @param constructorArgs The constructor args for the event wrapper class.
     * @param <E> The class of {@code event}.
     * @return A event wrapper.
     */
    public static <E> AwsEventWrapper<E> getWrapper(E event, Object...constructorArgs) {
        return defaultInstance.getCustomWrapper(event, constructorArgs);
    }

    /**
     * Get an event wrapper that can handle the given event.
     * This refers instance-level-defined event wrapper mapping.
     * @param event One of AWS lambda events.
     * @param constructorArgs The constructor args for the event wrapper class.
     * @param <E> The class of {@code event}.
     * @return A event wrapper.
     */
    public <E> AwsEventWrapper<E> getCustomWrapper(E event, Object...constructorArgs) {
        validateHandleable(event);
        return createWrapperOf(event, constructorArgs);
    }

    private void validateHandleable(Object event) {
        Class<?> eventClass = event.getClass();
        String packageName = eventClass.getPackageName();
        boolean withAwsLambdaEventPackage = AWS_LAMBDA_EVENTS_PACKAGE.equals(packageName);
        boolean isHandleableByWrappers = eventWrappingManual.contains(eventClass);
        if ( !withAwsLambdaEventPackage || !isHandleableByWrappers )
            throw new UnknownError(String.format("Class %s is unknown", eventClass));
    }


    private <E> AwsEventWrapper<E> createWrapperOf(E event, Object[] constructorArgs) {
        EventWrapperSpec specification = getEventWrapperSpec(event);
        Class<? extends AwsEventWrapper<?>> eventWrapperClass = specification.getEventWrapperClass();
        return (AwsEventWrapper<E>) createInstance(eventWrapperClass, constructorArgs, specification);
    }

    private <E> EventWrapperSpec getEventWrapperSpec(E event) {
        EventWrapperSpec wrapperSpec = eventWrappingManual.getEventWrapperSpec(event.getClass());
        assert wrapperSpec != null;
        return wrapperSpec;
    }

    private AwsEventWrapper<?> createInstance(Class<? extends AwsEventWrapper<?>> eventWrapperClass,
                                              Object[] args, EventWrapperSpec spec) {
        boolean useClient = spec.getUseAwsClient();
        try {
            Constructor<? extends AwsEventWrapper<?>> constructor = eventWrapperClass.getDeclaredConstructor(
                    useClient? new Class[]{spec.getClientClass()} : null);
            return useClient? constructor.newInstance(args[0]): constructor.newInstance();
        } catch (InstantiationException | InvocationTargetException e) {
            logger.error("Error instantiating a event wrapper: {}", eventWrapperClass);
        } catch (IllegalAccessException e) {
            logger.error("Could not reach the event wrapper type: {}", eventWrapperClass);
        } catch (NoSuchMethodException e) {
            logger.error("Could not find a matching constructor for: {}", eventWrapperClass);
        }
        return null;
    }

    public static final class AwsEventWrappingManual {

        private final Map<Class<?>, EventEntry> eventEntryMap = new HashMap<>();

        public EventEntry whenEvent(Class<?> eventClass) {
            EventEntry eventEntry = new EventEntry(this, eventClass);
            this.eventEntryMap.put(eventClass, eventEntry);
            return eventEntry;
        }

        public AwsEventWrappingManual and() {
            return this;
        }

        protected boolean contains(Class<?> eventClass) {
            return this.eventEntryMap.containsKey(eventClass);
        }

        protected EventWrapperSpec getEventWrapperSpec(Class<?> eventClass) {
            if (this.contains(eventClass)) {
                EventEntry eventEntry = this.eventEntryMap.get(eventClass);
                return eventEntry.getDefaultEventWrapperSpec();
            } else {
                throw new IllegalArgumentException(String.format(
                        "Could not find a event wrapper specification for event type %s", eventClass));
            }
        }

        @Override
        public String toString() {
            return "AwsEventWrapperManual{" +
                    "eventEntryMap=" + eventEntryMap +
                    '}';
        }
    }

    public static final class EventEntry {

        private final AwsEventWrappingManual parent;
        private final LinkedList<EventWrapperSpec> wrappersForEvent;

        public EventEntry(AwsEventWrappingManual awsEventWrappingManual, Class<?> eventClass) {
            this.parent = awsEventWrappingManual;
            this.wrappersForEvent = new LinkedList<>();
        }

        public EventWrapperSpec wrappedBy(Class<? extends AwsEventWrapper<?>> eventWrapperClass) {
            EventWrapperSpec eventWrapperSpec = new EventWrapperSpec(this, eventWrapperClass);
            this.wrappersForEvent.addFirst(eventWrapperSpec);
            return eventWrapperSpec;
        }

        protected EventWrapperSpec getDefaultEventWrapperSpec() {
            return this.wrappersForEvent.getFirst();
        }

        @Override
        public String toString() {
            return "EventEntry{" +
                    "wrappersForEvent=" + wrappersForEvent +
                    '}';
        }
    }

    public static final class EventWrapperSpec {

        private final EventEntry parent;
        private final Class<? extends AwsEventWrapper<?>> eventWrapperClass;

        private Class<?> clientClass = null;
        private boolean useAwsClient = false;

        public EventWrapperSpec(EventEntry eventEntry, Class<? extends AwsEventWrapper<?>> eventWrapperClass) {
            this.parent = eventEntry;
            this.eventWrapperClass = eventWrapperClass;
        }


        public EventWrapperSpec useAwsClient(Class<?> clientClass) {
            this.clientClass = clientClass;
            this.useAwsClient = true;
            return this;
        }

        public AwsEventWrappingManual and() {
            return this.parent.parent;
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
}

