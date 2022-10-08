package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class HoyopassException
        extends RuntimeException implements ThrowerAware<Hoyopass> {

    private final Hoyopass hoyopass;

    public HoyopassException(Hoyopass hoyopass) {
        this.hoyopass = hoyopass;
    }

    public HoyopassException(Hoyopass hoyopass, Throwable cause) {
        super(cause);
        this.hoyopass = hoyopass;
    }

    public HoyopassException(Hoyopass hoyopass, String message) {
        super(message);
        this.hoyopass = hoyopass;
    }

    public HoyopassException(Hoyopass hoyopass, String message, Throwable cause) {
        super(message, cause);
        this.hoyopass = hoyopass;
    }

    @Override
    public Hoyopass getThrower() {
        return this.hoyopass;
    }
}
