package org.binchoo.paimonganyu.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"org.binchoo.paimonganyu.hoyopass.view"}) // todo: remove this line
public class PaimonGanyuChatbotMain {
    public static void main(String[] args) {
        SpringApplication.run(PaimonGanyuChatbotMain.class);
    }
}
