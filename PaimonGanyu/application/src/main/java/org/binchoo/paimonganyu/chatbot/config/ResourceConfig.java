package org.binchoo.paimonganyu.chatbot.config;

import org.binchoo.paimonganyu.chatbot.error.support.ErrorFallbacks;
import org.binchoo.paimonganyu.chatbot.view.resource.Images;
import org.binchoo.paimonganyu.chatbot.view.resource.QuickReplies;
import org.binchoo.paimonganyu.ikakao.type.QuickReply;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix="s3")
@PropertySource(value = "classpath:images.yaml", factory = YamlPropertySourceFactory.class)
public class ResourceConfig {

    private Map<String, String> images;

    @Bean
    public Images setupImages() {
        return new Images(images);
    }

    @Bean
    public QuickReplies setupQuickReplies() {
        QuickReplies quickReplies = new QuickReplies();

        quickReplies.add(ErrorFallbacks.Home,
                new QuickReply("처음으로", "block", null, "62a33634d3ab72600628e825", null));

        quickReplies.add(ErrorFallbacks.ScanHoyopass,
                new QuickReply("재촬영", "block", null, "62a3411cef4e2505c632204e", null));

        quickReplies.add(ErrorFallbacks.DeleteHoyopass,
                new QuickReply("통행증 삭제", "block", null, "62a59a68678c9b1480a53389", null));

        quickReplies.add(ErrorFallbacks.ValidationCs,
                new QuickReply("계정 유효성 안내", "block", null, "62a59aeb678c9b1480a53392", null));

        quickReplies.add(ErrorFallbacks.CommonCs,
                new QuickReply("메일 문의", "block", null, "62a59b0efa834474ed740da6", null));

        return quickReplies;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }
}
