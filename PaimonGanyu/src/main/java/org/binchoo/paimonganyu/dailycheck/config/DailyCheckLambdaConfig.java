package org.binchoo.paimonganyu.dailycheck.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.config.HoyoApiConfig;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.*;

@EnableDynamoDBRepositories(basePackages = "org.binchoo.paimonganyu.dailycheck.repository")
@ComponentScan(basePackages = "org.binchoo.paimonganyu.dailycheck")
@Import(HoyoApiConfig.class)
@Profile("dailycheck")
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
