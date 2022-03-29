package org.binchoo.paimonganyu.hoyopass.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.binchoo.paimonganyu.hoyopass.domain.driven.HoyopassSearchPort;

import java.util.ArrayList;
import java.util.List;

@ToString
@Builder
@Getter
public class UserHoyopass {

    private final int MAX_HOYOPASS_COUNT = 2;

    /**
     * 카카오 챗봇이 식별한 유저 고유 아이디
     */
    private String botUserId;

    /**
     * 유저의 통행증 리스트
     */
    private List<Hoyopass> hoyopasses;

    public UserHoyopass() {
        hoyopasses = new ArrayList<>();
    }

    public UserHoyopass(String botUserId) {
        this();
        this.botUserId = botUserId;
    }

    public UserHoyopass(String botUserId, List<Hoyopass> hoyopasses) {
        this.botUserId = botUserId;
        this.hoyopasses = hoyopasses;
    }

    public void addHoyopass(Hoyopass hoyopass) {
        this.checkHoyopassAppendable(hoyopass);
        hoyopasses.add(hoyopass);
    }

    private void checkHoyopassAppendable(Hoyopass hoyopass) {
        checkVacancy();
        checkDuplicate(hoyopass);
    }

    private void checkVacancy() {
        if (!this.hasVacancy()) {
            throw new IllegalStateException("A User cannot have more than " + MAX_HOYOPASS_COUNT + " hoyopasses.");
        }
    }

    private boolean hasVacancy() {
        return MAX_HOYOPASS_COUNT > this.getCount();
    }

    private void checkDuplicate(Hoyopass hoyopass) {
        if (this.hoyopasses.stream().anyMatch(it-> it.equals(hoyopass))) {
            throw new IllegalStateException(
                    String.format("You're trying to add duplicate Hoyopass(ltuid: %s) object.", hoyopass.getLtuid()));
        }
    }

    public void addHoyopass(String ltuid, String ltoken, HoyopassSearchPort hoyopassSearchPort) {
        Hoyopass newHoyopass = Hoyopass.builder()
                .ltuid(ltuid).ltoken(ltoken).build();

        this.addHoyopass(newHoyopass);
        newHoyopass.fillUids(hoyopassSearchPort);
    }

    public void deleteAt(int i) {
        if (0 < this.getCount())
            hoyopasses.remove(i);
    }

    public int getCount() {
        return hoyopasses.size();
    }
}
