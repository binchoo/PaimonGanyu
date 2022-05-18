package org.binchoo.paimonganyu.hoyoapi.webclient.async;

import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;

/**
 * @author : jbinchoo
 * @since : 2022-04-23
 */
public interface Retriable {

    Retry getRetryObject();
}
