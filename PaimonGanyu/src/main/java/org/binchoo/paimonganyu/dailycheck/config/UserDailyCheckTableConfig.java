package org.binchoo.paimonganyu.dailycheck.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@EnableDynamoDBRepositories(basePackages = "org.binchoo.paimonganyu.dailycheck.repository")
@Configuration
public class UserDailyCheckTableConfig {
}
