package org.binchoo.paimonganyu.hoyopass.driving;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/10/20
 */
public interface HoyopassSyncPort {

    /**
     * 통행증의 갱신이 필요한지 판단합니다.
     * @param pass 호요버스 통행증
     * @return 갱신이 필요할 경우 {@code true} 아니면 {@code false}
     */
    boolean syncRequired(Hoyopass pass);

    /**
     * 유저가 보유한 통행증의 갱신이 필요한 지 여부를 배열로 반환합니다.
     * @param user 유저 통행증
     * @return 통행증 갱신이 필요한지 나타내는 불리언 배열
     */
    boolean[] syncRequired(UserHoyopass user);

    /**
     * 실제 호요버스 정보와 동기화 된 통행증을 반환합니다.
     * @param pass 호요버스 통행증
     * @return 동기화 된 통행증
     */
    Hoyopass synchronize(Hoyopass pass);
}
