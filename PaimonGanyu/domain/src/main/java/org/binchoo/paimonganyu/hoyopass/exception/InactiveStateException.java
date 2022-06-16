package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class InactiveStateException extends UserHoyopassException {

    public InactiveStateException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public InactiveStateException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public InactiveStateException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public InactiveStateException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
