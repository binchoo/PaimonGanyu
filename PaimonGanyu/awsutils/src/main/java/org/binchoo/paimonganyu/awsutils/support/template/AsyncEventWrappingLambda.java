package org.binchoo.paimonganyu.awsutils.support.template;

import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.binchoo.paimonganyu.awsutils.support.AwsEventParserFactory;

/**
 * <p> 비동기 동작을 하는 (handler의 반환 타입이 없는) 람다 핸들러를 작성합니다.
 * <p> 이 람다 핸들러는 이벤트를 {@link AwsEventParser} 형태로 전달받아 {@code doHandle()}에서 처리합니다.
 * <p> 주의하세요. 람다의 핸들러 메서드를 지정할 때는 {@code doHandle()}이 아닌 {@code handler()} 메서드를 택해야 합니다.
 * @author : jbinchoo
 * @since : 2022-07-08
 * @param <T> {@code com.amazonaws.services.lambda.runtime.events}에 정의된 AWS 람다의 이벤트 타입.
 */
public abstract class AsyncEventWrappingLambda<T> {

    private final AwsEventParserFactory factory;

    public AsyncEventWrappingLambda() {
        this.lookupDependencies();
        this.factory = getAwsEventParserFactory();
    }
    
    public void handler(T event) {
        AwsEventParser<T> eventParser = factory.newWrapper(event, getConstructorArgs());
        this.doHandle(event, eventParser);
    }

    /**
     * Override this to use a custom {@link AwsEventParserFactory}.
     * @return default - {@code AwsEventWrapperFactory.getDefault()}
     */
    protected AwsEventParserFactory getAwsEventParserFactory() {
        return AwsEventParserFactory.getDefault();
    }

    /**
     * Override this when the construction of targeting {@link AwsEventParser}
     * requires some constructor arguments.
     * @return An array of argument values to be injected when the construction of {@link AwsEventParser}.
     */
    protected Object[] getConstructorArgs() {
        return new Object[] {};
    }

    /**
     * This method is called once in its construction phase.
     * Here you load all dependencies that you require.
     */
    protected abstract void lookupDependencies();

    /**
     * Implements a asynchronous handler for the aws lambda event.
     * You must not refer this method when assigning the lambda's {@CodeUri} property.
     * Refer to the '@code handler()` method instead.
     * @param eventParser An event wrapper that contains the received event.
     */
    protected abstract void doHandle(T event, AwsEventParser<T> eventParser);
}
