package org.binchoo.paimonganyu.infra.redeem.web;

import org.binchoo.paimonganyu.redeem.RedeemResultCallback;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driven.RedeemClientPort;

import java.util.Collection;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
public class RedeemClientAdapter implements RedeemClientPort {

    @Override
    public void redeem(Collection<RedeemTask> redeemTasks, RedeemResultCallback resultCallback) {

    }
}
