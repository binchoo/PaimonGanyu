package org.binchoo.paimonganyu.dailycheck.domain;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;

public class DailyCheckTaskSpec {

    /**
     * 일일체크 요청자의 봇 유저 아이디
     */
    String botUserId;

    /**
     * 미호요 크레덴셜
     */
    LtuidLtoken ltuidLtoken;

    public DailyCheckTaskSpec() {
    }

    public DailyCheckTaskSpec(String botUserId, LtuidLtoken ltuidLtoken) {
        this.botUserId = botUserId;
        this.ltuidLtoken = ltuidLtoken;
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }

    public LtuidLtoken getLtuidLtoken() {
        return ltuidLtoken;
    }

    public void setLtuidLtoken(LtuidLtoken ltuidLtoken) {
        this.ltuidLtoken = ltuidLtoken;
    }
}
