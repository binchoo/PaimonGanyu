package org.binchoo.paimonganyu.chatbot.config.aws;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@EnableDynamoDBRepositories(
        basePackages = "org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository")
@Configuration
public class UserHoyopassTableConfig {
}
