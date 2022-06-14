package org.binchoo.paimonganyu.dailycheck.driving;

import java.time.LocalDate;

/**
 * @author : jbinchoo
 * @since : 2022-04-13
 */
public interface DailyCheckPort {

    /**
     * 해당 봇 유저의 명의로 주어진 통행증을 사용해 호요랩에 출석체크 합니다.
     * @param botUserId 봇 유저 아이디
     * @param ltuid 통행증 아이디
     * @param ltoken 통행증 크레덴셜 토큰
     */
    void claimDailyCheckIn(String botUserId, String ltuid, String ltoken);

    /**
     * 해당 봇 유저가 주어진 통행증을 사용해 이미 호요랩에 출석체크 했는지 판단합니다.
     * @param botUserId 봇 유저 아이디
     * @param ltuid 통행증 아이디
     * @return 호요랩에 출석체크 했는지 여부. 판단 기준은 PaimonGanyu 시스템 이력으로 한다.
     * 호요랩 측과 직접 통신하지 않으므로 실제와 다를 수 있다.
     */
    boolean hasCheckedInToday(String botUserId, String ltuid);

    /**
     * 해당 봇 유저가 주어진 통행증을 사용해 지정된 날짜에 호요랩에 출석체크 했는지 판단합니다.
     * @param botUserId 봇 유저 아이디
     * @param ltuid 통행증 아이디
     * @param date 조회할 날짜
     * @return 호요랩에 출석체크 했는지 여부. 판단 기준은 PaimonGanyu 시스템 이력으로 한다.
     * 호요랩 측과 직접 통신하지 않으므로 실제와 다를 수 있다.
     */
    boolean hasCheckedIn(String botUserId, String ltuid, LocalDate date);
}
