package org.binchoo.paimonganyu.testconfig.hoyopass;

import org.binchoo.paimonganyu.hoyopass.PaimonGanyu;
import org.binchoo.paimonganyu.testconfig.TestAccountConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({PaimonGanyu.class, TestAccountConfig.class})
@ComponentScan("org.binchoo.paimonganyu.testconfig.hoyopass")
public class HoyopassIntegrationConfig {
}
