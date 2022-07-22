package org.binchoo.paimonganyu.lambda;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.infra.redeem.dynamo.repository.UserRedeemDynamoAdapter;
import org.binchoo.paimonganyu.infra.redeem.dynamo.repository.UserRedeemDynamoRepository;
import org.binchoo.paimonganyu.infra.redeem.s3.repository.RedeemCodeS3Adapter;
import org.binchoo.paimonganyu.lambda.config.*;
import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationPort;
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
public class RedeemUserDeliveryMain {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * @param redeemRepository from {@link UserRedeemTableConfig}
     */
    @Bean
    public RedeemTaskEstimationPort redeemTaskEstimationService(UserRedeemDynamoRepository redeemRepository) {
        return new RedeemTaskEstimator(
                new RedeemBloomFilter(new UserRedeemDynamoAdapter(redeemRepository)));
    }

    /**
     * @param amazonS3 from {@link S3ClientConfig}
     */
    @Bean
    public RedeemCodeCrudPort redeemCodeCrudPort(AmazonS3 amazonS3) {
        return new RedeemCodeS3Adapter(amazonS3);
    }
}
