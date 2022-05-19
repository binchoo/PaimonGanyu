package org.binchoo.paimonganyu.hoyoapi.webclient.async;

import reactor.util.retry.Retry;

/**
 * @author : jbinchoo
 * @since : 2022-04-23
 */
public interface Retriable {

    void setRetry(Retry retry);
    Retry getRetryObject();
}
