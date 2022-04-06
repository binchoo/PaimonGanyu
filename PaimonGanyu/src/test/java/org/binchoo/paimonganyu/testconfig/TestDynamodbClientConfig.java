package org.binchoo.paimonganyu.testconfig;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class TestDynamodbClientConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String endpoint;

    @Value("${amazon.dynamodb.region}")
    private String region;

    @Primary
    @Bean(name = {"testAmazonDynamoDB", "amazonDynamoDB"})
    public AmazonDynamoDB testAmazonDynamoDB(AWSCredentialsProvider credentialsProvider) {
        AmazonDynamoDBClientBuilder dynamoDBClientBuilder
                = AmazonDynamoDBClientBuilder.standard();
        dynamoDBClientBuilder.setEndpointConfiguration(endpointConfig());
        dynamoDBClientBuilder.setCredentials(credentialsProvider);
        System.out.println("Using test AmazonDynamoDB");
        return dynamoDBClientBuilder.build();
    }

    private AwsClientBuilder.EndpointConfiguration endpointConfig() {
        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }
}