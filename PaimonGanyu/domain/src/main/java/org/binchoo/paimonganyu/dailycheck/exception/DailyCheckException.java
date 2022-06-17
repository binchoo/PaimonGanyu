package org.binchoo.paimonganyu.dailycheck.exception;

import org.binchoo.paimonganyu.error.ThrowerAware;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
public class DailyCheckException
        extends RuntimeException implements ThrowerAware<UserHoyopass> {

    private final UserHoyopass userHoyopass;

    public DailyCheckException(UserHoyopass userHoyopass) {
        this.userHoyopass = userHoyopass;
    }

    public DailyCheckException(UserHoyopass userHoyopass, Throwable cause) {
        super(cause);
        this.userHoyopass = userHoyopass;
    }

    public DailyCheckException(UserHoyopass userHoyopass, String message) {
        super(message);
        this.userHoyopass = userHoyopass;
    }

    public DailyCheckException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(message, cause);
        this.userHoyopass = userHoyopass;
    }

    @Override
    public UserHoyopass getThrower() {
        return this.userHoyopass;
    }
}
