package org.binchoo.paimonganyu.lambda.dailycheck.dto;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simplified version of {@UserHoyopass}
 */
public class UserHoyopassMessage {

    private String botUserId;
    private List<HoyopassMessage> hoyopasses;

    public UserHoyopassMessage() { }

    public UserHoyopassMessage(UserHoyopass userHoyopass) {
        this.botUserId = userHoyopass.getBotUserId();
        this.hoyopasses = userHoyopass.listHoyopasses().stream()
                .map(HoyopassMessage::new)
                .collect(Collectors.toList());
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }

    public List<HoyopassMessage> getHoyopasses() {
        return hoyopasses;
    }

    public void setHoyopasses(List<HoyopassMessage> hoyopasses) {
        this.hoyopasses = hoyopasses;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public UserHoyopass toDomain() {
        return UserHoyopass.builder()
                .botUserId(botUserId)
                .hoyopasses(hoyopasses.stream()
                        .map(HoyopassMessage::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }
}

class HoyopassMessage {

    private HoyopassCredentials credentials;
    private List<Uid> uids;

    public HoyopassMessage() { }

    public HoyopassMessage(Hoyopass hoyopass) {
        this.credentials = hoyopass.getCredentials();
        this.uids = Collections.unmodifiableList(hoyopass.getUids());
    }

    public void setCredentials(HoyopassCredentials credentials) {
        this.credentials = credentials;
    }

    public void setUids(List<Uid> uids) {
        this.uids = uids;
    }

    public HoyopassCredentials getCredentials() {
        return credentials;
    }

    public List<Uid> getUids() {
        return uids;
    }

    public Hoyopass toDomain() {
        return Hoyopass.builder()
                .credentials(credentials)
                .createAt(LocalDateTime.now())
                .uids(List.copyOf(uids))
                .build();
    }
}
