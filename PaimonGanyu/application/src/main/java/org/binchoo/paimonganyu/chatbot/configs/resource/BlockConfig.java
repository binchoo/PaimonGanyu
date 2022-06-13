package org.binchoo.paimonganyu.chatbot.configs.resource;

import lombok.Setter;
import org.binchoo.paimonganyu.chatbot.configs.YamlPropertySourceFactory;
import org.binchoo.paimonganyu.chatbot.resources.BlockIds;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.Objects;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
@Setter
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:blocks.yaml", factory = YamlPropertySourceFactory.class)
public class BlockConfig {

    private Map<String, String> blockNameAndId;

    @Bean
    public BlockIds blockIds() {
        Objects.requireNonNull(blockNameAndId);
        return new BlockIds(blockNameAndId);
    }
}
