package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class QuantityException extends UserHoyopassException {

    public QuantityException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public QuantityException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public QuantityException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public QuantityException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
