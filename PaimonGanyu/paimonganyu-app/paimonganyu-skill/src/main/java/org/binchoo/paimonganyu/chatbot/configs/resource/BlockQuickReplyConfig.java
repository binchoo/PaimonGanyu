package org.binchoo.paimonganyu.chatbot.configs.resource;

import org.binchoo.paimonganyu.chatbot.configs.YamlPropertySourceFactory;
import org.binchoo.paimonganyu.chatbot.resources.BlockSpec;
import org.binchoo.paimonganyu.chatbot.resources.Blocks;
import lombok.Setter;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
@Setter
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:blocks.yaml", factory = YamlPropertySourceFactory.class)
public class BlockQuickReplyConfig {

    private Map<String, BlockSpec> blocks;
    private Blocks blocksSingleton;
    private QuickReplies quickReplies;

    @PostConstruct
    public void initResources() {
        this.blocksSingleton = new Blocks(this.blocks);
        this.quickReplies = new QuickReplies(this.blocks);
    }

    @Bean
    public Blocks blocks() {
        return this.blocksSingleton;
    }

    @Bean
    public QuickReplies quickReplies() {
        return this.quickReplies;
    }
}
