package org.binchoo.paimonganyu.testconfig.dailycheck;

import org.binchoo.paimonganyu.dailycheck.config.DailyCheckConfig;
import org.binchoo.paimonganyu.testconfig.AwsContextConfig;
import org.binchoo.paimonganyu.testconfig.TestAmazonClientsConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({AwsContextConfig.class, TestAmazonClientsConfig.class, DailyCheckConfig.class})
@ComponentScan("org.binchoo.paimonganyu.testconfig.dailycheck")
@Configuration
public class DailyCheckIntegrationConfig {
}
