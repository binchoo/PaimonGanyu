package org.binchoo.paimonganyu.chatbot.config.view;

import org.binchoo.paimonganyu.chatbot.resource.Images;
import org.binchoo.paimonganyu.chatbot.resource.QuickReplies;
import org.binchoo.paimonganyu.chatbot.view.error.ErrorResponseTemplate;
import org.binchoo.paimonganyu.chatbot.view.error.ErrorResponseView;
import org.binchoo.paimonganyu.chatbot.view.uid.ListUidsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class ViewConfig {

    @Bean
    public ViewResolver viewResolver() {
        return new BeanNameViewResolver();
    }

    @Bean
    public View ListUidsView(@Autowired Images images, @Autowired QuickReplies quickReplies) {
        ListUidsView view = new ListUidsView(images, quickReplies);
        view.setPrettyPrint(true);
        return view;
    }
}
