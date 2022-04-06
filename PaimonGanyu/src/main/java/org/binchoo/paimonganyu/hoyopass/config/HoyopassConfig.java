package org.binchoo.paimonganyu.hoyopass.config;

import org.binchoo.paimonganyu.globalconfig.DynomdbClientConfig;
import org.binchoo.paimonganyu.globalconfig.HoyoApiConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({HoyoApiConfig.class, DynomdbClientConfig.class})
@Configuration
public class HoyopassConfig {
}
