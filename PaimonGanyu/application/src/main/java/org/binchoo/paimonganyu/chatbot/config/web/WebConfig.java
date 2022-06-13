package org.binchoo.paimonganyu.chatbot.config.web;

import org.binchoo.paimonganyu.chatbot.controller.resolver.id.UserIdArgumentResolver;
import org.binchoo.paimonganyu.chatbot.controller.resolver.param.ActionParamArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ActionParamArgumentResolver actionParamArgumentResolver;

    @Autowired
    private UserIdArgumentResolver userIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(actionParamArgumentResolver);
        resolvers.add(userIdArgumentResolver);
    }
}
