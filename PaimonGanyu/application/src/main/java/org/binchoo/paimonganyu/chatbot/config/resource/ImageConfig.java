package org.binchoo.paimonganyu.chatbot.config.resource;

import lombok.Setter;
import org.binchoo.paimonganyu.chatbot.config.YamlPropertySourceFactory;
import org.binchoo.paimonganyu.chatbot.resource.Images;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Setter
@Configuration
@ConfigurationProperties(prefix="s3")
@PropertySource(value = "classpath:images.yaml", factory = YamlPropertySourceFactory.class)
public class ImageConfig {

    private String prefix;
    private Map<String, String> imageNameAndUrl;

    @Bean
    public Images loadImages() {
        this.concatSuffix();
        return new Images(imageNameAndUrl);
    }

    private void concatSuffix() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : imageNameAndUrl.entrySet()) {
            String imgName = entry.getKey();
            String objSuffix = entry.getValue();
            sb.setLength(0);
            sb.append(prefix);
            imageNameAndUrl.put(imgName, sb.append(objSuffix).toString());
        }
    }
}
