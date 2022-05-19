package org.binchoo.paimonganyu.lambda.dailycheck.dto;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.util.Collections;
import java.util.List;

/**
 * Simplified version of {@UserHoyopass}
 */
public class UserHoyopassMessage {

    private String botUserId;
    private List<Hoyopass> hoyopasses;

    public UserHoyopassMessage() { }

    public UserHoyopassMessage(UserHoyopass userHoyopass) {
        this.botUserId = userHoyopass.getBotUserId();
        this.hoyopasses = Collections.unmodifiableList(userHoyopass.getHoyopasses());
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }

    public List<Hoyopass> getHoyopasses() {
        return hoyopasses;
    }

    public void setHoyopasses(List<Hoyopass> hoyopasses) {
        this.hoyopasses = hoyopasses;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public UserHoyopass toDomain() {
        return UserHoyopass.builder()
                .botUserId(botUserId)
                .hoyopasses(hoyopasses)
                .build();
    }
}
