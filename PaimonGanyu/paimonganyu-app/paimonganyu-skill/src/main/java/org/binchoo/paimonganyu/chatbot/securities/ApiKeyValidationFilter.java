package org.binchoo.paimonganyu.chatbot.securities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jbinchoo
 * @since 2022/09/24
 */
public class ApiKeyValidationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyValidationFilter.class);
    private static final String HEADER_X_API_KEY = "X-Api-Key";
    private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    private final String expectedApiKey;

    public ApiKeyValidationFilter(String apiKey) {
        this.expectedApiKey = apiKey;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        String actualApiKey = httpRequest.getHeader(HEADER_X_API_KEY);

        if (expectedApiKey.equals(actualApiKey)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String clientIp = httpRequest.getHeader(HEADER_X_FORWARDED_FOR);
            logger.warn("[Security] Unauthorized api-key: {} from {}", actualApiKey, clientIp);
        }
    }
}
