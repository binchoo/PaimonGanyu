package org.binchoo.paimonganyu.awsutils.support;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.AmazonS3;
import org.binchoo.paimonganyu.awsutils.dynamo.DynamodbEventWrapper;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.awsutils.sns.SNSEventWrapper;
import org.binchoo.paimonganyu.awsutils.sqs.SQSEventWrapper;

/**
 * pakcage-private
 * The default configurer that configures {@AwsEventWrapperFacory}'s mapping behaviors.
 */
class DefaultMappingConfigurer implements AwsEventWrapperMappingConfigurer {

    @Override
    public void configure(AwsEventWrapperFactory.AwsEventWrappingManual wrappingManual) {
        wrappingManual
                .whenEvent(SQSEvent.class)
                        .wrappedBy(SQSEventWrapper.class)
                .and()
                .whenEvent(SNSEvent.class)
                        .wrappedBy(SNSEventWrapper.class)
                .and()
                .whenEvent(S3Event.class)
                        .wrappedBy(S3EventObjectReader.class)
                            .useAwsClient(AmazonS3.class)
                .and()
                .whenEvent(DynamodbEvent.class)
                        .wrappedBy(DynamodbEventWrapper.class)
                            .useAwsClient(DynamoDBMapper.class);
    }
}