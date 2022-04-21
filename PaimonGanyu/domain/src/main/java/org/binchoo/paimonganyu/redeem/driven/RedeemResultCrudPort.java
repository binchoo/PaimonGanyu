package org.binchoo.paimonganyu.redeem.driven;

import org.binchoo.paimonganyu.redeem.RedeemResult;

import java.util.Collection;

/**
 * @author : jbinchoo
 * @since : 2022-04-21
 */
public interface RedeemResultCrudPort {

    void save(RedeemResult redeemResult);
    void saveAll(Collection<RedeemResult> redeemResults);
}
