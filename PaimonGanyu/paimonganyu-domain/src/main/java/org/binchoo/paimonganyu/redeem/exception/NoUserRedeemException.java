package org.binchoo.paimonganyu.redeem.exception;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.UserHoyopassException;

/**
 * @author : jbinchoo
 * @since : 2022-06-13
 */
public class NoUserRedeemException extends UserHoyopassException {

    public NoUserRedeemException(UserHoyopass userHoyopass) {
        super(userHoyopass);
    }

    public NoUserRedeemException(UserHoyopass userHoyopass, Throwable cause) {
        super(userHoyopass, cause);
    }

    public NoUserRedeemException(UserHoyopass userHoyopass, String message) {
        super(userHoyopass, message);
    }

    public NoUserRedeemException(UserHoyopass userHoyopass, String message, Throwable cause) {
        super(userHoyopass, message, cause);
    }
}
