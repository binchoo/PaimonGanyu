package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface EstimationOption {

    List<UserHoyopass> getUsers();
    List<RedeemCode> getCodes();
}
