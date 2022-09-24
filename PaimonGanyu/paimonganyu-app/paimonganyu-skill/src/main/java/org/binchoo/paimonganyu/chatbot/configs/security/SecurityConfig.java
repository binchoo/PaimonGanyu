package org.binchoo.paimonganyu.chatbot.configs.security;

import org.binchoo.paimonganyu.chatbot.securities.ApiKeyValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author jbinchoo
 * @since 2022/09/24
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth.apikey}")
    private String expectedApiKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/ikakao/**")
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .addFilterBefore(new ApiKeyValidationFilter(expectedApiKey), BasicAuthenticationFilter.class)
            .csrf().disable();
    }
}
