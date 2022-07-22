package org.binchoo.paimonganyu.dailycheck.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.UserHoyopassException;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class NoUserDailyCheckException extends UserHoyopassException {

    public NoUserDailyCheckException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public NoUserDailyCheckException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public NoUserDailyCheckException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public NoUserDailyCheckException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
