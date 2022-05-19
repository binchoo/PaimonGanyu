package org.binchoo.paimonganyu.lambda.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@Configuration
public class S3ClientConfig {

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.defaultClient();
    }
}
