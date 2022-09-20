package org.binchoo.paimonganyu.chatbot.configs.view;

import org.binchoo.paimonganyu.chatbot.resources.Blocks;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.chatbot.views.dailycheck.DailyCheckListView;
import org.binchoo.paimonganyu.chatbot.views.hoyopass.HoyopassListView;
import org.binchoo.paimonganyu.chatbot.views.redeem.RedeemListTextView;
import org.binchoo.paimonganyu.chatbot.views.redeem.RedeemListView;
import org.binchoo.paimonganyu.chatbot.views.traveler.TravelerStatusView;
import org.binchoo.paimonganyu.chatbot.views.uid.UidListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
    @Bean
    public UidListView uidListView(@Autowired Images images,
                                   @Autowired QuickReplies quickReplies) {
        return new UidListView(images, quickReplies);
    }

    @Lazy
    @Bean
    public HoyopassListView hoyopassListView(@Autowired Images images,
                                             @Autowired QuickReplies quickReplies,
                                             @Autowired Blocks blocks) {
        return new HoyopassListView(images, quickReplies, blocks);
    }

    @Lazy
    @Bean
    public TravelerStatusView travelerStatusView(@Autowired Images images,
                                                 @Autowired QuickReplies quickReplies) {
        return new TravelerStatusView(images, quickReplies);
    }

    @Lazy
    @Bean
    public DailyCheckListView dailyCheckListView(@Autowired Images images,
                                                 @Autowired QuickReplies quickReplies,
                                                 @Autowired Blocks blocks) {
        return new DailyCheckListView(images, quickReplies, blocks);
    }

    @Lazy
    @Bean
    public RedeemListView redeemListView(@Autowired Images images,
                                         @Autowired QuickReplies quickReplies) {
        return new RedeemListView(images, quickReplies);
    }

    @Lazy
    @Bean
    public RedeemListTextView redeemListTextView(@Autowired QuickReplies quickReplies) {
        return new RedeemListTextView(quickReplies);
    }
}
