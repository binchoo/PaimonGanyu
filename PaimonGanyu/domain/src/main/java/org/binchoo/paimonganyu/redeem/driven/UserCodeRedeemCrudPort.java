package org.binchoo.paimonganyu.redeem.driven;

import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserCodeRedeem;

import java.util.List;

/**
 * 유저의 코드 리딤 이력 저장소에 접근하는 포트입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface UserCodeRedeemCrudPort {

    /**
     * 주어진 {@link UserCodeRedeem}이 표상하는 코드 리딤 행위와
     * 동일한 과거 이력을 조회합니다.
     * @return 코드 리딤 과거 이력인 {@link UserCodeRedeem} 리스트,
     * 이력이 없으면 빈 리스트.
     */
    List<UserCodeRedeem> findMatches(UserCodeRedeem userCodeRedeem);

    /**
     * 주어진 {@link UserCodeRedeem}이 표상하는 코드 리딤 행위가
     * 이미 과거에 수행된 이력이 있는지 여부.
     * @return 코드 리딤 수행이력이 있을 경우 true, 없을 때 false.
     */
    boolean existMatches(UserCodeRedeem userCodeRedeem);

    /**
     * 주어진  {@link RedeemCode}로 진행된 코드 리딤 이력을 조회합니다.
     * @param redeemCode 리딤코드
     * @return 코드 리딤 이력, 없을 경우 빈 리스트.
     */
    List<UserCodeRedeem> findByRedeemCode(RedeemCode redeemCode);

    /**
     * 모든 코드 리딤 이력을 조회합니다.
     * @return 모든 {@link UserCodeRedeem}의 리스트, 없을 경우 빈 리스트.
     */
    List<UserCodeRedeem> findAll();

    /**
     * 코드 리딤 이력을 저장합니다. 
     * @param userCodeRedeem 코드 리딤 이력 객체
     */
    UserCodeRedeem save(UserCodeRedeem userCodeRedeem);
}
