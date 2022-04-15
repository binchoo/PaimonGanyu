package org.binchoo.paimonganyu.testamazonclients;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:amazon.properties")
public class TestAmazonClientsConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamoEndpoint;

    @Value("${amazon.region}")
    private String region;

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Primary
    @Bean(name = {"testAmazonDynamoDB", "amazonDynamoDB"})
    public AmazonDynamoDB testAmazonDynamoDB(AWSCredentialsProvider credentialsProvider) {
        AmazonDynamoDBClientBuilder dynamoDBClientBuilder
                = AmazonDynamoDBClientBuilder.standard();
        dynamoDBClientBuilder.setEndpointConfiguration(dynamoEndpointConfig());
        dynamoDBClientBuilder.setCredentials(credentialsProvider);
        System.out.println("Using test AmazonDynamoDB");
        return dynamoDBClientBuilder.build();
    }

    private AwsClientBuilder.EndpointConfiguration dynamoEndpointConfig() {
        return new AwsClientBuilder.EndpointConfiguration(dynamoEndpoint, region);
    }

    @Primary
    @Bean
    public AWSSimpleSystemsManagement testSsmClient(AWSCredentialsProvider credentialsProvider) {
        AWSSimpleSystemsManagementClientBuilder ssmBuilder = AWSSimpleSystemsManagementClientBuilder.standard();
        ssmBuilder.setRegion(region);
        ssmBuilder.setCredentials(credentialsProvider);
        System.out.println("Using test ssmClient");
        return ssmBuilder.build();
    }
}