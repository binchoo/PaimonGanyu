package org.binchoo.paimonganyu.redeem.driven;

import org.binchoo.paimonganyu.redeem.RedeemCode;

import java.util.List;

/**
 * 리딤 코드 저장소에 접근하는 포트입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface RedeemCodeCrudPort {

    List<RedeemCode> findAll();
}
