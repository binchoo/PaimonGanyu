package org.binchoo.paimonganyu.hoyopass.config;

import org.binchoo.paimonganyu.hoyoapi.config.HoyoApiConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(HoyoApiConfig.class)
@Configuration
public class ImportHoyolabApi {
}
