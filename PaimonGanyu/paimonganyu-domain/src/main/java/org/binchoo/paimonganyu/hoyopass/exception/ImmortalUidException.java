package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class ImmortalUidException extends UserHoyopassException {

    public ImmortalUidException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public ImmortalUidException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public ImmortalUidException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public ImmortalUidException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
