package org.binchoo.paimonganyu.lambda.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@EnableDynamoDBRepositories(
        basePackages = "org.binchoo.paimonganyu.infra.redeem.dynamo.repository")
@Configuration
public class UserRedeemTableConfig {
}
