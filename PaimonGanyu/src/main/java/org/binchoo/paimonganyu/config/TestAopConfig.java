package org.binchoo.paimonganyu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "org.binchoo.paimonganyu.hoyoapi.error.aspect")
@Configuration
public class TestAopConfig {
}
