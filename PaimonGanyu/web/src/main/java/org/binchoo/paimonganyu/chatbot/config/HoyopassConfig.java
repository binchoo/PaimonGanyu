package org.binchoo.paimonganyu.hoyopass.config;

import org.binchoo.paimonganyu.globalconfig.DynamoDBClientConfig;
import org.binchoo.paimonganyu.globalconfig.HoyoApiConfig;
import org.binchoo.paimonganyu.globalconfig.SsmClientConfig;
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
