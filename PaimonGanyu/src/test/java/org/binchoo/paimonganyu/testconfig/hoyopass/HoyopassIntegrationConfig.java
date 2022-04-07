package org.binchoo.paimonganyu.testconfig.hoyopass;

import org.binchoo.paimonganyu.hoyopass.PaimonGanyu;
import org.binchoo.paimonganyu.testconfig.AwsContextConfig;
import org.binchoo.paimonganyu.testconfig.TestAmazonClientsConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// If you're going to bootstrap a springboot test, import order below should be maintained.
// The bean 'amazonDynamoDB' in TestDynamodbClientConfig.class is the primary.
// It is the trick not to make followed 'amazonDynamoDB' bean creation in springboot test throw a duplication error.
@Import({AwsContextConfig.class, TestAmazonClientsConfig.class, PaimonGanyu.class})
@ComponentScan("org.binchoo.paimonganyu.testconfig.hoyopass")
@Configuration
public class HoyopassIntegrationConfig {
}
