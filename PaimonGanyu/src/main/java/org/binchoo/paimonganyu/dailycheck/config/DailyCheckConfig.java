package org.binchoo.paimonganyu.dailycheck.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.globalconfig.DynomdbClientConfig;
import org.binchoo.paimonganyu.globalconfig.HoyoApiConfig;
import org.binchoo.paimonganyu.globalconfig.SqsClientConfig;
import org.binchoo.paimonganyu.hoyopass.config.UserHoyopassTableConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = {
        "org.binchoo.paimonganyu.dailycheck",
        "org.binchoo.paimonganyu.hoyopass.infra.dynamo.repository"
})
@Import({
        HoyoApiConfig.class, DynomdbClientConfig.class, SqsClientConfig.class,
        UserHoyopassTableConfig.class
})
@Configuration
public class DailyCheckConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
