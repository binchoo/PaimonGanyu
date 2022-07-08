package org.binchoo.paimonganyu.awsutils.support;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.AmazonS3;
import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.binchoo.paimonganyu.awsutils.dynamo.DynamodbEventParser;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.awsutils.sns.SNSEventParser;
import org.binchoo.paimonganyu.awsutils.sqs.SQSEventParser;

/**
 * <p> {@link AwsEventParser} 구현체에 대한 팩토리 클래스입니다.
 * <p> 팩토리가 이벤트 파서를 생성하기 위해'{@code AWS이벤트 - 이벤트 파서}'매핑을 정의하는 {@link WrappingManual} 객체가 필요합니다.
 * <p> 이미 정의된 기본 매핑을 쓰려면 {@code getDefault()} 인스턴스를 이용합니다.
 * <p> 매핑을 커스터마이징 하려면 별도로 구현한 {@link AwsEventParserFactoryConfigurer}를 주입합니다.
 * 해당 매핑 설정을 갖는 새로운 팩토리 객체를 만들 수 있습니다.
 * <p> {@link AwsEventParser}가 이 팩토리를 통해 만들어지려면, 접근 가능한 하나 이상의 생성자를 갖고 있어야 합니다.
 * @author : jbinchoo
 * @since : 2022-04-24
 */
public class AwsEventParserFactory {

    private static final String AWS_LAMBDA_EVENTS_PACKAGE = "com.amazonaws.services.lambda.runtime.events";
    private static AwsEventParserFactory DEFAULT_INSTANCE;

    private final WrappingManual manual;

    /**
     * Get the default instance of {@link AwsEventParserFactory}
     * @return The default singleton instance of {@link AwsEventParserFactory}
     */
    public static AwsEventParserFactory getDefault() {
        if (DEFAULT_INSTANCE == null)
            DEFAULT_INSTANCE = AwsEventParserFactory.newInstance(new DefaultFactoryConfigurer());
        return DEFAULT_INSTANCE;
    }

    /**
     * Create a custom {@link AwsEventParserFactory} instance
     * whose mapping behavior is customized by the given {@link AwsEventParserFactoryConfigurer}.
     * @param configurer The configurer that customizes the {@code Event to EventParser} mappings.
     */
    public static AwsEventParserFactory newInstance(AwsEventParserFactoryConfigurer configurer) {
        return (configurer == null)? getDefault() : new AwsEventParserFactory(configurer);
    }

    /**
     * Create a {@link AwsEventParserFactory} with given {@link AwsEventParserFactoryConfigurer}
     * @param configurer The configurer that customizes the {@code Event to EventParser} mappings.
     */
    private AwsEventParserFactory(AwsEventParserFactoryConfigurer configurer) {
        this.manual = new WrappingManual();
        configurer.configure(this.manual);
    }

    /**
     * Get an {@link AwsEventParser} that can handle the event of the given type.
     * @param event An aws lambda event object you want to handle.
     * @param constructorArgs The constructor args required by the event parser construction.
     * @param <E> The type of the {@code event}.
     * @return An event parser instance that can handle the {@code event}.
     * @throws UnknownError When the type of {@code event} is unknown to {@link WrappingManual}.
     * @throws IllegalArgumentException When matched {@link AwsEventParser} requires a constructor args,
     * but those are not provided.
     */
    public <E> AwsEventParser<E> newParser(E event, Object...constructorArgs) {
        Class<?> eClass = event.getClass();
        MappingEntry<E> mappingEntry = mappingEntryOf((Class<E>) eClass);
        return mappingEntry.newParser(constructorArgs);
    }

    private <E> MappingEntry<E> mappingEntryOf(Class<E> eClass) {
        assertHandleable(eClass);
        return manual.getMappingEntry(eClass);
    }

    private void assertHandleable(Class<?> eClass) {
        boolean hasMatchingEventParser = manual.contains(eClass);
        boolean isLambdaEvent = AWS_LAMBDA_EVENTS_PACKAGE.equals(eClass.getPackageName());
        if ( !hasMatchingEventParser || !isLambdaEvent)
            throw new UnknownError(String.format("Class %s is unknown", eClass));
    }

    @Override
    public String toString() {
        return "AwsEventParserFactory{" +
                "eventWrappingManual=" + manual +
                '}';
    }

    /**
     * The default configurer that configures {@link AwsEventParserFactory}'s mapping behaviors.
     */
    private static final class DefaultFactoryConfigurer implements AwsEventParserFactoryConfigurer {

        @Override
        public void configure(WrappingManual manual) {
            manual
                    .whenEvent(SQSEvent.class)
                        .wrapIn(SQSEventParser.class)
                    .and()
                    .whenEvent(SNSEvent.class)
                        .wrapIn(SNSEventParser.class)
                    .and()
                    .whenEvent(S3Event.class)
                        .wrapIn(S3EventObjectReader.class).argTypes(AmazonS3.class)
                    .and()
                    .whenEvent(DynamodbEvent.class)
                        .wrapIn(DynamodbEventParser.class).argTypes(DynamoDBMapper.class);
        }
    }
}
