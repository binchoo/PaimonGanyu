package org.binchoo.paimonganyu.chatbot.config.resource;

import lombok.Setter;
import org.binchoo.paimonganyu.chatbot.config.YamlPropertySourceFactory;
import org.binchoo.paimonganyu.chatbot.resource.BlockIds;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
public class BlockConfig {

    private Map<String, String> blockNameAndId = new HashMap<>();

    @Bean
    public BlockIds loadBlocks() {
        return new BlockIds(blockNameAndId);
    }
}
