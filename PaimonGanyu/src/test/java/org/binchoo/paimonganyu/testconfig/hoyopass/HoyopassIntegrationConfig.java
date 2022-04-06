package org.binchoo.paimonganyu.testconfig.hoyopass;

import org.binchoo.paimonganyu.hoyopass.PaimonGanyu;
import org.binchoo.paimonganyu.testconfig.AwsContextConfig;
import org.binchoo.paimonganyu.testconfig.TestDynamodbClientConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// This import order is very important.
// The bean 'amazonDynamoDB' in TestDynamodbClientConfig.class is the primary bean.
// That's why followed 'amazonDynamoDB' bean in PaimonGanyu.class cannot make a duplication error.
@Import({AwsContextConfig.class, TestDynamodbClientConfig.class, PaimonGanyu.class})
@ComponentScan("org.binchoo.paimonganyu.testconfig.hoyopass")
@Configuration
public class HoyopassIntegrationConfig {
}
