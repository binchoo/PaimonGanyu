package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class NoHoyopassException extends UserHoyopassException {

    public NoHoyopassException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public NoHoyopassException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public NoHoyopassException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public NoHoyopassException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
