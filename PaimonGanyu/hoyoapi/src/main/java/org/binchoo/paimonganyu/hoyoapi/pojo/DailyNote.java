package org.binchoo.paimonganyu.hoyoapi.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Setter
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DailyNote {

    /**
     * 현재 레진 수치
     */
    private int currentResin;

    /**
     * 레진 최대 수치 = 160
     */
    private int maxResin;

    /**
     * 레진 회복에 걸릴 시간 secs = [0, 60*8*160=76800]
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(as = Long.class)
    private long resinRecoveryTime;

    /**
     * 오늘 완료한 일일 임무
     */
    private int finishedTaskNum;

    /**
     * 일일 임무 최대 개수 = 4
     */
    private int totalTaskNum;

    /**
     * TODO: 이 필드의 의미를 파악
     */
    private boolean isExtraTaskRewardReceived;

    /**
     * 주간 보스 레진 할인 남은 횟수
     */
    private int remainResinDiscountNum;

    /**
     * 주간 보스 레진 할인 최대 횟수 = 3
     */
    private int resinDiscountNumLimit;

    /**
     * 현재 탐사 의뢰 파견 수 = [0, 5]
     */
    private int currentExpeditionNum;

    /**
     * 탐사 의뢰 파견 정보
     */
    private List<Expedition> expeditions;

    /**
     * 현재 미수령 선계 보화
     */
    private int currentHomeCoin;

    /**
     * 최대 선계 보화 = 2400
     */
    private int maxHomeCoin;

    /**
     * 선계 보화 회복에 걸릴 시간 secs
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(as = Long.class)
    private long homeCoinRecoveryTime;

    @ToString
    @Setter
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Expedition {

        /**
         * 탐사 의뢰 파견된 캐릭터 이미지
         */
        private String avatarSideIcon;

        /**
         * 탐사 의뢰 파견 상태 [Ongoing|Finished]
         */
        private String status;

        /**
         * 탐사 의뢰 파견 완료까지 남은 시간 secs
         */
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(as = Long.class)
        private long remainedTime;

        public boolean isFinished() {
            return "Finished".equals(status);
        }

        public boolean isOngoin() {
            return "Ongoing".equals(status);
        }
    }
}
