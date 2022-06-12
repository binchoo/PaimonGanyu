package org.binchoo.paimonganyu.chatbot.config;

import org.binchoo.paimonganyu.chatbot.view.Images;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix="s3")
@PropertySource(value = "classpath:images.yaml", factory = YamlPropertySourceFactory.class)
public class ImageConfig {

    private Map<String, String> images;

    @Bean
    public Images setupImages() {
        return new Images(images);
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }
}
