package org.binchoo.paimonganyu.redeem.options;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public class GivenUsersOption implements EstimationOption {

    private final RedeemCodeCrudPort redeemCodeCrudPort;
    private final List<UserHoyopass> userHoyopasses;

    public GivenUsersOption(RedeemCodeCrudPort redeemCodeCrudPort, List<UserHoyopass> givenUserHoyopasses) {
        this.redeemCodeCrudPort = redeemCodeCrudPort;
        this.userHoyopasses = new ArrayList<>();
        this.userHoyopasses.addAll(givenUserHoyopasses);
    }

    @Override
    public List<UserHoyopass> getUsers() {
        return userHoyopasses;
    }

    @Override
    public List<RedeemCode> getCodes() {
        return redeemCodeCrudPort.findAll();
    }
}
