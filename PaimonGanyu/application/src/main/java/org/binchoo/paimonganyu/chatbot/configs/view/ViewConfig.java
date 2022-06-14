package org.binchoo.paimonganyu.chatbot.configs.view;

import org.binchoo.paimonganyu.chatbot.resources.BlockIds;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.dailycheck.DailyCheckTrialListView;
import org.binchoo.paimonganyu.chatbot.views.hoyopass.HoyopassListView;
import org.binchoo.paimonganyu.chatbot.views.traveler.TravelerStatusView;
import org.binchoo.paimonganyu.chatbot.views.uid.UidListView;
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
    public UidListView listUidsView(@Autowired Images images,
                                    @Autowired QuickReplies quickReplies) {
        return new UidListView(images, quickReplies);
    }

    @Lazy
    @Bean
    public HoyopassListView listHoyopassesView(@Autowired Images images,
                                               @Autowired QuickReplies quickReplies,
                                               @Autowired BlockIds blockIds) {
        return new HoyopassListView(images, quickReplies, blockIds);
    }

    @Lazy
    @Bean
    public TravelerStatusView travelerStatusView(@Autowired Images images,
                                                 @Autowired QuickReplies quickReplies) {
        return new TravelerStatusView(images, quickReplies);
    }

    @Lazy
    @Bean
    public DailyCheckTrialListView dailyCheckTrialListView(@Autowired Images images,
                                                           @Autowired QuickReplies quickReplies) {
        return new DailyCheckTrialListView(images, quickReplies);
    }
}
