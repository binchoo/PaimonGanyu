package org.binchoo.paimonganyu.chatbot.configs.resource;

import org.binchoo.paimonganyu.chatbot.configs.YamlPropertySourceFactory;
import org.binchoo.paimonganyu.chatbot.resources.Images;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Setter
@Configuration
@ConfigurationProperties(prefix = "s3")
@PropertySource(value = "classpath:images.yaml", factory = YamlPropertySourceFactory.class)
public class ImageConfig {

    private String prefix;
    private Map<String, String> images;

    @Bean
    public Images images() {
        return new Images(this.concatUrl());
    }

    private Map<String, String> concatUrl() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : this.images.entrySet()) {
            String imageName = entry.getKey();
            String imageS3Suffix = entry.getValue();
            sb.setLength(0);
            sb.append(prefix);
            this.images.put(imageName, sb.append(imageS3Suffix).toString());
        }
        return this.images;
    }
}
