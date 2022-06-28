package org.binchoo.paimonganyu.traveler.driven;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.traveler.TravelerStatus;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
public interface GameRecordClientPort {

    /**
     * 주어진 통행증 소유 모든 여행자의 실시간 상태값 집합
     * @param user 통행증 소유 유저
     * @param comparator {@link TravelerStatus}를 정렬할 {@link Comparator}, null 주입시 클래스의 기본 설정을 적용하라.
     * @return 여행자 상태값 집합
     */
    Collection<TravelerStatus> getStatusOf(UserHoyopass user, Comparator<TravelerStatus> comparator);

    /**
     * 주어진 통행증 소유 모든 여행자의 실시간 상태값 집합
     * @param pass 통행증
     * @param comparator {@link TravelerStatus}를 정렬할 {@link Comparator}, null 주입시 클래스의 기본 설정을 적용하라.
     * @return 여행자 상태값 집합
     */
    Collection<TravelerStatus> getStatusOf(Hoyopass pass, Comparator<TravelerStatus> comparator);

    /**
     * 주어진 UID를 갖는 여행자의 실시간 상태값
     * @param uid 여행자 UID
     * @param pass uid를 소유를 증명할 통행증
     * @return 여행자 상태값
     */
    Optional<TravelerStatus> getStatusOf(Uid uid, Hoyopass pass);

    void setTravelerStatusComparator(Comparator<TravelerStatus> comparator);
}
