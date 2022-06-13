package org.binchoo.paimonganyu.chatbot.config.view;

import org.binchoo.paimonganyu.chatbot.resource.Images;
import org.binchoo.paimonganyu.chatbot.resource.QuickReplies;
import org.binchoo.paimonganyu.chatbot.view.uid.ListUidsView;
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
//     * @Deprecated aws-serverless-java-container에서 이용할 수 없다.
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
