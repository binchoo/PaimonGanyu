package org.binchoo.paimonganyu.lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.AwsEventWrapper;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository.UserHoyopassDynamoAdapter;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository.UserHoyopassDynamoRepository;
import org.binchoo.paimonganyu.infra.redeem.dynamo.repository.UserRedeemDynamoAdapter;
import org.binchoo.paimonganyu.infra.redeem.dynamo.repository.UserRedeemDynamoRepository;
import org.binchoo.paimonganyu.lambda.config.*;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationService;
import org.binchoo.paimonganyu.service.redeem.RedeemBloomFilter;
import org.binchoo.paimonganyu.service.redeem.RedeemTaskEstimator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@Import({
        SqsClientConfig.class, S3ClientConfig.class,
        DynamoDBClientConfig.class, UserRedeemTableConfig.class, UserHoyopassTableConfig.class
})
@Configuration
public class NewRedeemCodeDeliveryMain {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AwsEventWrapper<S3Event> s3EventWrapper(AmazonS3 s3Client) {
        return new S3EventObjectReader(s3Client, objectMapper());
    }

    /**
     * @param redeemRepository from {@link UserRedeemTableConfig}
     */
    @Bean
    public RedeemTaskEstimationService redeemTaskEstimationService(UserRedeemDynamoRepository redeemRepository) {
        return new RedeemTaskEstimator(
                new RedeemBloomFilter(new UserRedeemDynamoAdapter(redeemRepository)));
    }

    /**
     * @param hoyopassRepository from {@link UserHoyopassTableConfig}
     */
    @Bean
    public UserHoyopassCrudPort userHoyopassCrudPort(UserHoyopassDynamoRepository hoyopassRepository) {
        return new UserHoyopassDynamoAdapter(hoyopassRepository);
    }
}
