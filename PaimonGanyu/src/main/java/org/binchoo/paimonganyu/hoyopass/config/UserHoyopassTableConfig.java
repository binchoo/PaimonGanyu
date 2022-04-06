package org.binchoo.paimonganyu.hoyopass.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@EnableDynamoDBRepositories(
        basePackages = "org.binchoo.paimonganyu.hoyopass.infra.dynamo")
@Configuration
public class UserHoyopassTableConfig {
}
