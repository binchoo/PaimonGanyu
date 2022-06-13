package org.binchoo.paimonganyu.chatbot.config.resource;

import lombok.Setter;
import org.binchoo.paimonganyu.chatbot.config.YamlPropertySourceFactory;
import org.binchoo.paimonganyu.chatbot.resource.BlockIds;
import org.binchoo.paimonganyu.chatbot.resource.FallbackMethods;
import org.binchoo.paimonganyu.chatbot.resource.QuickReplies;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.Objects;

@Setter
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:quicks.yaml", factory = YamlPropertySourceFactory.class)
public class QuickReplyConfig {

    private Map<String, String> blockNameAndLabel;

    @Autowired
    private BlockIds blockRegistry;

    @Bean
    public QuickReplies quickReplies() {
        QuickReplies quickReplies = new QuickReplies();
        Objects.requireNonNull(blockNameAndLabel);
        for (Map.Entry<String, String> quick : blockNameAndLabel.entrySet()) {
            String blockName = quick.getKey();
            String quickLabel = quick.getValue();

            String blockId = blockRegistry.findByName(blockName);

            quickReplies.add(FallbackMethods.get(blockName),
                    new QuickReply(quickLabel, "block", null, blockId, null));
        }
        return quickReplies;
    }
}
