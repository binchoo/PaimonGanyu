package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class ManyHoyopassException extends UserHoyopassException {

    public ManyHoyopassException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public ManyHoyopassException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public ManyHoyopassException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public ManyHoyopassException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
