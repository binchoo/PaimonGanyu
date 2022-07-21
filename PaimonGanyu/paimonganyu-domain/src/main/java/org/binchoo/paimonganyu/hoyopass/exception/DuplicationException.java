package org.binchoo.paimonganyu.hoyopass.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class DuplicationException extends UserHoyopassException {

    public DuplicationException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public DuplicationException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public DuplicationException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public DuplicationException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
