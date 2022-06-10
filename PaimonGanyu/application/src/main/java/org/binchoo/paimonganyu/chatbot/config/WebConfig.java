package org.binchoo.paimonganyu.chatbot.config;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.chatbot.skilldatabind.ActionParamExtractor;
import org.binchoo.paimonganyu.chatbot.skilldatabind.UserIdExtractor;
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

    private final ActionParamExtractor actionParamExtractor;
    private final UserIdExtractor userIdExtractor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(actionParamExtractor);
        resolvers.add(userIdExtractor);
    }
}
