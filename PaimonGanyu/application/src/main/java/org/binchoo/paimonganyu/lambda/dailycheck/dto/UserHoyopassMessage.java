package org.binchoo.paimonganyu.lambda.dailycheck.dto;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.util.Arrays;

/**
 * Simplified version of {@UserHoyopass}
 */
public class UserHoyopassMessage {

    private String botUserId;
    private LtuidLtoken[] ltuidLtokens;

    public UserHoyopassMessage() { }

    public UserHoyopassMessage(UserHoyopass userHoyopass) {
        this(userHoyopass.getBotUserId(), new LtuidLtoken[userHoyopass.getCount()]);
        for (int i = 0; i < ltuidLtokens.length; i++) {
            Hoyopass hoyopass = userHoyopass.getHoyopasses().get(i);
            this.ltuidLtokens[i] = new LtuidLtoken(hoyopass.getLtuid(), hoyopass.getLtoken());
        }
    }

    public UserHoyopassMessage(String botUserId, LtuidLtoken[] ltuidLtokens) {
        this.botUserId = botUserId;
        this.ltuidLtokens = ltuidLtokens;
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }

    public LtuidLtoken[] getLtuidLtokens() {
        return ltuidLtokens;
    }

    public void setLtuidLtokens(LtuidLtoken[] ltuidLtokens) {
        this.ltuidLtokens = ltuidLtokens;
    }

    @Override
    public String toString() {
        return "UserHoyopassMessage{" +
                "botUserId='" + botUserId + '\'' +
                ", ltuidLtokens=" + Arrays.toString(ltuidLtokens) +
                '}';
    }
}
