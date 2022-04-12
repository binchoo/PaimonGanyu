package org.binchoo.paimonganyu.lambda.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = {
        "org.binchoo.paimonganyu.dailycheck",
        "org.binchoo.paimonganyu.hoyopass.infra.dynamo.repository"
})
@Import({
        HoyoApiConfig.class, DynamoDBClientConfig.class, SqsClientConfig.class,
        UserHoyopassTableConfig.class
})
@Configuration
public class DailyCheckConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
