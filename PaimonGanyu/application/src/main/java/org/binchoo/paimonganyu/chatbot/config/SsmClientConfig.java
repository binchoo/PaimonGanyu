package org.binchoo.paimonganyu.chatbot.config;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.context.annotation.Bean;

public class SsmClientConfig {

    @Bean
    AWSSimpleSystemsManagement awsSimpleSystemsManagement() {
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }
}