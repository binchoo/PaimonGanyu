package org.binchoo.paimonganyu.chatbot.configs.view;

import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.uid.ListUidsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
@Configuration
public class ViewConfig {


//    /**
//     * @deprecated aws-serverless-java-container에서 이용할 수 없다.
//     */
//    @Bean
//    public ViewResolver viewResolver() {
//        return new BeanNameViewResolver();
//    }

    @Lazy
    @Bean
    public ListUidsView listUidsView(@Autowired Images images, @Autowired QuickReplies quickReplies) {
        return new ListUidsView(images, quickReplies);
    }
}
