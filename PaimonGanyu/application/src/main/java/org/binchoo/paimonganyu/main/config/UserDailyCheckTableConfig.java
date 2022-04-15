package org.binchoo.paimonganyu.main.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@EnableDynamoDBRepositories(
        basePackages = "org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository")
@Configuration
public class UserDailyCheckTableConfig {
}
