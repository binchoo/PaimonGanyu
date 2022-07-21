package org.binchoo.paimonganyu.chatbot.configs.aws;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@EnableDynamoDBRepositories(
        basePackages = {
                "org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository",
                "org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository"
        })
@Configuration
public class DynamoTableConfig {
}
