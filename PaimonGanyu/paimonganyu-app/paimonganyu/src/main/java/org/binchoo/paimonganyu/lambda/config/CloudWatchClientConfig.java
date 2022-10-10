package org.binchoo.paimonganyu.lambda.config;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jbinchoo
 * @since 2022/10/10
 */
@Configuration
public class CloudWatchClientConfig {

    @Bean
    public AmazonCloudWatch amazonCloudWatch() {
        return AmazonCloudWatchClientBuilder.defaultClient();
    }
}
