package org.binchoo.paimonganyu.chatbot.config.view;

import org.binchoo.paimonganyu.chatbot.resource.Images;
import org.binchoo.paimonganyu.chatbot.resource.QuickReplies;
import org.binchoo.paimonganyu.chatbot.view.uid.ListUidsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
@Configuration
public class ViewConfig {

    @Bean
    public ViewResolver viewResolver() {
        return new BeanNameViewResolver();
    }

    @Bean
    public View ListUidsView(@Autowired Images images, @Autowired QuickReplies quickReplies) {
        return new ListUidsView(images, quickReplies);
    }
}
