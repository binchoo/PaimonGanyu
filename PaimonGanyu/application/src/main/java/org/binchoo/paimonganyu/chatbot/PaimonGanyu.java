package org.binchoo.paimonganyu.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@SpringBootApplication(scanBasePackages = {
        "org.binchoo.paimonganyu.chatbot",
        "org.binchoo.paimonganyu.infra.hoyopass",
        "org.binchoo.paimonganyu.infra.traveler",
        "org.binchoo.paimonganyu.infra.dailycheck",
        "org.binchoo.paimonganyu.service.hoyopass",
        "org.binchoo.paimonganyu.service.traveler",
        "org.binchoo.paimonganyu.service.dailycheck",
})
public class PaimonGanyu {

    public static void main(String[] args) {
        SpringApplication.run(PaimonGanyu.class);
    }
}
