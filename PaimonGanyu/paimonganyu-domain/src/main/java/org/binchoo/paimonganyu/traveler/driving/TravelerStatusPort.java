package org.binchoo.paimonganyu.traveler.driving;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.traveler.TravelerStatus;

import java.util.Collection;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
public interface TravelerStatusPort {
    /**
     * 주어진 유저 소유의 통행증 소유의 여행자들의 상태를 통합 반환한다.
     * @param user 유저
     * @return 유저 소유 여행자들의 게임 스테이터스 집합
     */
    Collection<TravelerStatus> getCurrentStatus(UserHoyopass user);

    /**
     * 통행증 소유의 여행자들의 상태를 통합 반환한다.
     * @param pass 통행증
     * @return 유저 소유 여행자들의 게임 스테이터스 집합
     */
    Collection<TravelerStatus> getCurrentStatus(Hoyopass pass);
}
