package org.binchoo.paimonganyu;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDynamoDBRepositories({"org.binchoo.paimonganyu.hoyopass.infra"})
@EnableJpaRepositories({"org.binchoo.paimonganyu.hoyopass.view"}) // todo: remove this line
public class PaimonGanyuApp {
    public static void main(String[] args) {
        SpringApplication.run(PaimonGanyuApp.class);
    }
}
