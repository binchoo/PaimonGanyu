package org.binchoo.paimonganyu.dailycheck.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.hoyoapi.config.HoyoApiConfig;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableDynamoDBRepositories(basePackages = {
        "org.binchoo.paimonganyu.dailycheck.repository", "org.binchoo.paimonganyu.hoyopass.infra.dynamo.repository"
})
@ComponentScan(basePackages = {
        "org.binchoo.paimonganyu.dailycheck", "org.binchoo.paimonganyu.hoyopass.infra.dynamo.repository"
})
@Import(HoyoApiConfig.class)
@Configuration
public class DailyCheckLambdaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.defaultClient();
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
