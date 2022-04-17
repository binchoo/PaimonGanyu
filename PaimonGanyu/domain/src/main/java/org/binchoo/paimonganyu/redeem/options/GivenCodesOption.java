package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.redeem.RedeemCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class GivenCodesOption implements EstimationOption {

    private final UserHoyopassCrudPort userHoyopassCrudPort;
    private final List<RedeemCode> redeemCodes;

    public GivenCodesOption(UserHoyopassCrudPort userHoyopassCrudPort, List<RedeemCode> givenCodes) {
        this.userHoyopassCrudPort = userHoyopassCrudPort;
        this.redeemCodes = new ArrayList<>();
        this.redeemCodes.addAll(givenCodes);
    }

    @Override
    public List<UserHoyopass> getUsers() {
        return userHoyopassCrudPort.findAll();
    }

    @Override
    public List<RedeemCode> getCodes() {
        return redeemCodes;
    }
}
