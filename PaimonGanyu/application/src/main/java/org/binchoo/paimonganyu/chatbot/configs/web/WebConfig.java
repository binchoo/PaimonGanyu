package org.binchoo.paimonganyu.chatbot.configs.web;

import org.binchoo.paimonganyu.chatbot.controllers.resolvers.clientextra.ClientExtraResolver;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.id.UserIdResolver;
import org.binchoo.paimonganyu.chatbot.controllers.resolvers.param.ActionParamResolver;
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
    private ActionParamResolver actionParamResolver;

    @Autowired
    private UserIdResolver userIdResolver;

    @Autowired
    private ClientExtraResolver clientExtraResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(
                List.of(actionParamResolver, userIdResolver, clientExtraResolver));
    }
}
