package org.binchoo.paimonganyu.chatbot.config;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.resolver.param.ActionParamArgumentResolver;
import org.binchoo.paimonganyu.chatbot.resolver.id.UserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ActionParamArgumentResolver actionParamArgumentResolver;
    private final UserIdArgumentResolver userIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(actionParamArgumentResolver);
        resolvers.add(userIdArgumentResolver);
    }
}
