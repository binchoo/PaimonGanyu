package org.binchoo.paimonganyu.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"org.binchoo.paimonganyu.chatbot.view"}) // todo: remove this line
@SpringBootApplication(scanBasePackages = {
        "org.binchoo.paimonganyu.chatbot",
        "org.binchoo.paimonganyu.infra.hoyopass",
        "org.binchoo.paimonganyu.service.hoyopass",
})
public class PaimonGanyuChatbotMain {

    public static void main(String[] args) {
        SpringApplication.run(PaimonGanyuChatbotMain.class);
    }
}
