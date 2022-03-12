package org.binchoo.paimonganyu.hoyopass.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String endpoint;

    @Value("${amazon.dynamodb.region}")
    private String region;

    @Bean
    public AmazonDynamoDB amazonDynamoDB(AWSCredentialsProvider credentialsProvider) {
        AmazonDynamoDBClientBuilder dynamoDBClientBuilder
                = AmazonDynamoDBClientBuilder.standard();
        dynamoDBClientBuilder.setEndpointConfiguration(endpointConfig());
        dynamoDBClientBuilder.setCredentials(credentialsProvider);
        return dynamoDBClientBuilder.build();
    }

    private AwsClientBuilder.EndpointConfiguration endpointConfig() {
        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }
}