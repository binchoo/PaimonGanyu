package org.binchoo.paimonganyu.redeem.driven;

import org.binchoo.paimonganyu.redeem.RedeemCode;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface RedeemCodeCrudPort {

    List<RedeemCode> findAll();
}
