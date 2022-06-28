package org.binchoo.paimonganyu.chatbot;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import org.junit.jupiter.api.Test;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
class PaimonGanyuLambdaTest {

    @Test
    void bootstreap() throws ContainerInitializationException {
        var handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                .defaultProxy()
                .asyncInit()
                .profiles("test")
                .springBootApplication(PaimonGanyu.class)
                .buildAndInitialize();
    }
}