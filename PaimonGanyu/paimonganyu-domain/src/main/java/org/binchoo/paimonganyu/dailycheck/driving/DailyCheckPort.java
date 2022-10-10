package org.binchoo.paimonganyu.dailycheck.driving;

import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.time.LocalDate;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-13
 */
public interface DailyCheckPort {
    /**
     * 주어진 통행증으로 수행한 출석 체크 이력을 반환한다.
     * 이 때, 이력을 타임스탬프 기준으로 내림차순 정렬하여 상위 {@code count}개만 반환한다.
     * @param user 유저 통행증
     * @param count 반환할 이력 개수
     * @return 출석 체크 수행 이력
     */
    List<List<UserDailyCheck>> historyOfUser(UserHoyopass user, int count);

    /**
     * 주어진 통행증으로 수행한 출석 체크 이력을 반환한다.
     * 이 때, 이력을 타임스탬프 기준으로 내림차순 정렬하여 상위 {@code count}개만 반환한다.
     * @param pass 통행증
     * @param count 반환할 이력 개수
     * @return 출석 체크 수행 이력
     */
    List<UserDailyCheck> historyOfUser(String botUserId, Hoyopass pass, int count);

    /**
     * 유저 소유의 모든 통행증으로 출석 체크 요청을 수행한다.
     * @param userHoyopass 유저 통행증
     * @return 출석 체크 수행 이력
     */
    List<UserDailyCheck> claimDailyCheckIn(UserHoyopass userHoyopass);

    /**
     * 유저 소유의 통행증 하나로 출석 체크 요청을 수행한다.
     * @param botUserId 통행증 소유를 증명할 유저 아이디
     * @param pass 유저 소유의 통행증
     * @return 출석 체크 수행 이력
     */
    UserDailyCheck claimDailyCheckIn(String botUserId, Hoyopass pass);

    /**
     * 해당 봇 유저의 명의로 주어진 통행증을 사용해 호요랩에 출석체크 합니다.
     * @param botUserId 봇 유저 아이디
     * @param ltuid 통행증 아이디
     * @param ltoken 통행증 크레덴셜 토큰
     */
    UserDailyCheck claimDailyCheckIn(String botUserId, String ltuid, String ltoken);

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

    /**
     * 오늘 수행된 호요랩 출석체크 성공률을 계산하여 반환합니다.
     * @return 오늘자 출석체크 성공률
     */
    double getCheckedInRate();
}
