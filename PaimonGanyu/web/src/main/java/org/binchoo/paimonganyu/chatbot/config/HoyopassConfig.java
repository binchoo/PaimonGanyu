package org.binchoo.paimonganyu.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({HoyoApiConfig.class, DynamoDBClientConfig.class, SsmClientConfig.class})
@Configuration
public class HoyopassConfig {

    @Bean
    public String publicKeyName(@Value("${hoyopass.security.publickeyname}") String publicKeyName) {
        return publicKeyName;
    }

    @Bean
    public String privateKeyName(@Value("${hoyopass.security.privatekeyname}") String privateKeyName) {
        return privateKeyName;
    }
}
