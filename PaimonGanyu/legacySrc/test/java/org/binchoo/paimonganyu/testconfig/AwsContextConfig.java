package org.binchoo.paimonganyu.testconfig;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AwsContextConfig {

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }
}