package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class InactiveStateException extends HoyopassException {

    public InactiveStateException(Hoyopass hoyopass) {
        super(hoyopass);
    }

    public InactiveStateException(Hoyopass hoyopass, Throwable cause) {
        super(hoyopass, cause);
    }

    public InactiveStateException(Hoyopass hoyopass, String message) {
        super(hoyopass, message);
    }

    public InactiveStateException(Hoyopass hoyopass, String message, Throwable cause) {
        super(hoyopass, message, cause);
    }
}
