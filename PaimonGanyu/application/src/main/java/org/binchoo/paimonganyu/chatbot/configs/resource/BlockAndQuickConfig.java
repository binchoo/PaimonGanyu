package org.binchoo.paimonganyu.chatbot.configs.resource;

import lombok.Data;
import lombok.Setter;
import org.binchoo.paimonganyu.chatbot.configs.YamlPropertySourceFactory;
import org.binchoo.paimonganyu.chatbot.resources.BlockIds;
import org.binchoo.paimonganyu.chatbot.resources.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resources.QuickReplies;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
@Setter
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:blocks.yaml", factory = YamlPropertySourceFactory.class)
public class BlockAndQuickConfig {

    private Map<String, ReadBlock> blocks;
    private Map<String, String> blockNameAndId;
    private QuickReplies quickReplies;

    @PostConstruct
    public void initRepos() {
        this.blockNameAndId = new HashMap<>();
        this.quickReplies = new QuickReplies();
        blocks.forEach((name, block)-> {
            blockNameAndId.put(name, block.getId());
            quickReplies.add(FallbackMethods.findByBlockName(name),
                    new QuickReply(block.getLabel(), "block", null, block.getId(), null));
        });
    }

    @Bean
    public BlockIds blockIds() {
        return new BlockIds(blockNameAndId);
    }

    @Bean
    public QuickReplies quickReplies() {
        return this.quickReplies;
    }

    @Data
    @Setter
    private static class ReadBlock {

        private String id;
        private String label;
    }
}
