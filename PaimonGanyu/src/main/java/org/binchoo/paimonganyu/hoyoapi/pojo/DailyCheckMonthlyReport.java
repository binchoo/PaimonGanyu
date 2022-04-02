package org.binchoo.paimonganyu.hoyoapi.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DailyCheckMonthlyReport {

    /**
     * 이번달 총 출석 체크 횟수
     */
    int totalSignDay;

    /**
     * 오늘 날짜
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate today;

    /**
     * TODO: 의미 파악
     */
    boolean isSign;

    /**
     * TODO: 의미 파악
     */
    boolean firstBind;

    /**
     * TODO: 의미 파악
     */
    boolean isSub;

    /**
     * 유저 지역. 통행증의 모든 UID를 대상으로 (모든 지역) 출석 체크가 진행되므로 의미가 없는 필드로 사료됨.
     */
    String region;
}
