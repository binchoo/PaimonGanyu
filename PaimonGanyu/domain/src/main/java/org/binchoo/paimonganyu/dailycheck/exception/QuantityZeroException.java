package org.binchoo.paimonganyu.dailycheck.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class QuantityZeroException extends DailyCheckException {

    public QuantityZeroException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public QuantityZeroException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public QuantityZeroException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public QuantityZeroException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
