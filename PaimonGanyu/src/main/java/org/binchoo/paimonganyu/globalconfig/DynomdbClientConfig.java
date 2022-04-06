package org.binchoo.paimonganyu.globalconfig;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynomdbClientConfig {

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        System.out.println("Using prod AmazonDynamoDB");
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
