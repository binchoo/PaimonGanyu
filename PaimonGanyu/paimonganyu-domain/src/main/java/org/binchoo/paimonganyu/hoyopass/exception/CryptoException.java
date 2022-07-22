package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.SecureHoyopassCredentials;

/**
 * @author jbinchoo
 * @since 2022/06/12
 */
public class CryptoException
        extends RuntimeException implements ThrowerAware<SecureHoyopassCredentials> {

    private final SecureHoyopassCredentials sch;

    public CryptoException(SecureHoyopassCredentials sch) {
        super();
        this.sch = sch;
    }

    public CryptoException(SecureHoyopassCredentials sch, String message) {
        super(message);
        this.sch = sch;
    }

    public CryptoException(SecureHoyopassCredentials sch, String message, Throwable cause) {
        super(message, cause);
        this.sch = sch;
    }

    public CryptoException(SecureHoyopassCredentials sch, Throwable cause) {
        super(cause);
        this.sch = sch;
    }

    @Override
    public SecureHoyopassCredentials getThrower() {
        return this.sch;
    }
}
