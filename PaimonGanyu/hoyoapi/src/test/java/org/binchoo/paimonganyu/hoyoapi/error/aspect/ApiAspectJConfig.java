package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.binchoo.paimonganyu.hoyoapi.support.DsHeaderGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "org.binchoo.paimonganyu.hoyoapi")
@Configuration
public class ApiAspectJConfig {

    @Bean
    public DsHeaderGenerator dsHeaderGenerator() {
        return DsHeaderGenerator.getDefault();
    }
}
