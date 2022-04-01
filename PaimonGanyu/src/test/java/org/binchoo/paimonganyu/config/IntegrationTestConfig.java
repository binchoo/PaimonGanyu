package org.binchoo.paimonganyu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@ComponentScan(basePackages = "org.binchoo.paimonganyu")
@Configuration
@Profile("integ")
public class IntegrationTestConfig {
}
