package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class QuantityExceedException extends UserHoyopassException {

    public QuantityExceedException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public QuantityExceedException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public QuantityExceedException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public QuantityExceedException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
