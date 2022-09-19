package org.binchoo.paimonganyu.redeem.driven;

import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;

import java.util.Collection;
import java.util.List;

/**
 * 유저의 코드 리딤 이력 저장소에 접근하는 포트입니다.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public interface UserRedeemCrudPort {

    /**
     * 주어진 {@link UserRedeem}이 표상하는 코드 리딤 행위와
     * 동일한 과거 이력을 조회합니다.
     * @return 코드 리딤 과거 이력인 {@link UserRedeem} 리스트,
     * 이력이 없으면 빈 리스트.
     */
    List<UserRedeem> findMatches(UserRedeem userRedeem);

    /**
     * 주어진 {@link UserRedeem}이 표상하는 코드 리딤 행위가
     * 이미 과거에 수행된 이력이 있는지 여부.
     * @return 코드 리딤 수행이력이 있을 경우 true, 없을 때 false.
     */
    boolean existMatches(UserRedeem userRedeem);

    /**
     * 주어진  {@link RedeemCode}로 진행된 코드 리딤 이력을 조회합니다.
     * @param redeemCode 리딤코드
     * @return 코드 리딤 이력, 없을 경우 빈 리스트.
     */
    List<UserRedeem> findByRedeemCode(RedeemCode redeemCode);

    /**
     * 모든 코드 리딤 이력을 조회합니다.
     * @return 모든 {@link UserRedeem}의 리스트, 없을 경우 빈 리스트.
     */
    List<UserRedeem> findAll();

    /**
     * 코드 리딤 이력을 저장합니다. 
     * @param userRedeem 코드 리딤 이력 객체
     */
    UserRedeem save(UserRedeem userRedeem);

    /**
     * 코드 리딤 이력을 저장합니다.
     * @param userRedeems 코드 리딤 이력 집단
     */
    List<UserRedeem> saveAll(Collection<UserRedeem> userRedeems);

    /**
     * 유저에게 수행된 코드 리딤 이력을 모두 조회합니다.
     * @param user 유저 통행증
     * @return 코드 리딤 이력 집단
     */
    List<UserRedeem> findByUser(UserHoyopass user);

    /**
     * 유저에게 수행된 코드 리딤 이력 중 최신 n개를 조회합니다.
     * @param user 유저 통행증
     * @param limit 조회할 최신 이력 개수
     * @return 코드 리딤 이력 집단
     */
    List<UserRedeem> findByUser(UserHoyopass user, int limit);
}
