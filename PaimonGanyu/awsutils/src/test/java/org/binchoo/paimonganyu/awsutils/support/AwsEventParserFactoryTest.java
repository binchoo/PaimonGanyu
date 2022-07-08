package org.binchoo.paimonganyu.awsutils.support;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.*;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.binchoo.paimonganyu.awsutils.dynamo.DynamodbEventParser;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.awsutils.sns.SNSEventParser;
import org.binchoo.paimonganyu.awsutils.sqs.SQSEventParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author : jbinchoo
 * @since : 2022-04-24
 */
class AwsEventParserFactoryTest {

    AwsEventParserFactory defaultFactory = AwsEventParserFactory.getDefault();

    @DisplayName("AwsEventWrapperFactory 타입이 잘 로드된다.")
    @Test
    void clinit() {
        var foobar = AwsEventParserFactory.class;
    }

    @DisplayName("AwsEventWrapperFactory의 커스텀 버전을 생성할 수 있다.")
    @Test
    void configure() {
        var factory = AwsEventParserFactory.newInstance(mappingManual -> {
            mappingManual.whenEvent(SQSEvent.class)
                    .wrapIn(CustomSQSEventParser.class);
        });
        var event = new SQSEvent();
        var expectedWrapper = new CustomSQSEventParser();

        var eventWrapper = factory.newParser(event);

        assertThat(eventWrapper).hasSameClassAs(expectedWrapper);
    }

    @DisplayName("SQSEvent에 대해 명세된 이벤트 파서를 반환한다.")
    @Test
    void givenSQSEvent_returnsSpecifiedEventWrapper() {
        var event = new SQSEvent();
        var exepectedWraper = new SQSEventParser();

        var eventWrapper = defaultFactory.newParser(event);

        assertThat(eventWrapper).hasSameClassAs(exepectedWraper);
    }

    @DisplayName("SNSEvent에 대해 명세된 이벤트 파서를 반환한다.")
    @Test
    void givenSNSEvent_returnsSpecifiedEventWrapper() {
        var event = new SNSEvent();
        var exepectedWraper = new SNSEventParser();

        var eventWrapper = defaultFactory.newParser(event);

        assertThat(eventWrapper).hasSameClassAs(exepectedWraper);
    }

    @DisplayName("S3Event에 대해 명세된 이벤트 파서를 반환한다.")
    @Test
    void givenS3Event_returnsSpecifiedEventWrapper() {
        var event = new S3Event();
        var s3Client = AmazonS3ClientBuilder.defaultClient();
        var exepectedWraper = new S3EventObjectReader(s3Client);

        var eventWrapper = defaultFactory.newParser(event,s3Client);

        assertThat(eventWrapper).hasSameClassAs(exepectedWraper);
    }

    @DisplayName("DynamodbEvent에 대해 명세된 이벤트 파서를 반환한다.")
    @Test
    void givenDynamodbEvent_returnsSpecifiedEventWrapper() {
        var event = new DynamodbEvent();
        var dynamodbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
        var exepectedWraper = new DynamodbEventParser(dynamodbMapper);

        var eventWrapper = defaultFactory.newParser(event, dynamodbMapper);

        assertThat(eventWrapper).hasSameClassAs(exepectedWraper);
    }

    @DisplayName("적절한 생성자 인자 없이는 S3Event에 대해 명세된 이벤트 파서를 반환할 수 없다.")
    @Test
    void givenS3EventAndInvalidConstructorArgs_cannotCreateAEventWrapper() {
        var event = new S3Event();
        var badClient = AmazonDynamoDBClientBuilder.defaultClient();

        assertThrows(IllegalArgumentException.class, ()-> {
            defaultFactory.newParser(event, null);
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            defaultFactory.newParser(event);
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            defaultFactory.newParser(event, badClient);
        });
    }

    @SuppressWarnings("Allowed null arguments.")
    @DisplayName("적절한 생성자 인자 없이는 DynamodbEvent에 대해 명세된 이벤트 파서를 반환할 수 없다.")
    @Test
    void givenDynamodbEventAndInvalidConstructorArgs_cannotCreateAEventWrapper() {
        var event = new DynamodbEvent();
        var badClient = AmazonS3ClientBuilder.defaultClient();
        assertThrows(IllegalArgumentException.class, ()-> {
            defaultFactory.newParser(event, null);
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            defaultFactory.newParser(event);
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            defaultFactory.newParser(event, badClient);
        });
    }

    public static final class CustomSQSEventParser implements AwsEventParser<SQSEvent> {

        @Override
        public <T> List<T> extractPojos(SQSEvent event, Class<T> clazz) {
            return null;
        }
    }

    @DisplayName("매핑 메뉴얼에 없는 람다 이벤트로는 이벤트 파서를 얻을 수 없다.")
    @Test
    void givenUnregisterdLambdaEvent_cannotCreateAEventWrapper() {
        var event = new ScheduledEvent();
        assertThrows(UnknownError.class, ()-> {
            defaultFactory.newParser(event);
        });
    }
}