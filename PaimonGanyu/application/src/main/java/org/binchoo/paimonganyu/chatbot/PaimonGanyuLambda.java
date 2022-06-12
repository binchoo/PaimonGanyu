package org.binchoo.paimonganyu.chatbot;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
@Slf4j
public class PaimonGanyuLambda implements RequestStreamHandler {

    private static final String PROFILE = System.getenv("profile");

    private final SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    public PaimonGanyuLambda() throws ContainerInitializationException {
        handler = new SpringProxyHandlerBuilder<AwsProxyRequest>()
                .defaultProxy()
                .asyncInit()
                .profiles(PROFILE)
                .configurationClasses(PaimonGanyu.class)
                .buildAndInitialize();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.proxyStream(input, output, context);
    }
}
