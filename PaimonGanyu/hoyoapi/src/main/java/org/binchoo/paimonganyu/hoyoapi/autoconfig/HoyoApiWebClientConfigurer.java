package org.binchoo.paimonganyu.hoyoapi.autoconfig;

import org.binchoo.paimonganyu.hoyoapi.DataSwitchConfigurer;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.support.DefaultDataSwitchConfigurer;
import org.binchoo.paimonganyu.hoyoapi.support.DsHeaderGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author : jbinchoo
 * @since : 2022-04-20
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {
        "org.binchoo.paimonganyu.hoyoapi.webclient",
        "org.binchoo.paimonganyu.hoyoapi.error.aspect"
})
@Configuration
public class HoyoApiWebClientConfigurer {

    @ConditionalOnMissingBean(type = "DsHeaderGenerator")
    @Bean
    public DsHeaderGenerator dsHeaderGenerator() {
        return DsHeaderGenerator.getDefault();
    }

    @Bean
    public DataSwitchConfigurer dataSwitchConfigurer(HoyolabGameRecordApi gameRecordApi) {
        return new DefaultDataSwitchConfigurer(gameRecordApi);
    }
}
