package org.binchoo.paimonganyu.hoyopass.domain;

import lombok.Builder;
import lombok.Getter;
import org.binchoo.paimonganyu.hoyopass.domain.driving.HoyopassSearchPort;

import java.util.ArrayList;
import java.util.List;

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
        this.checkHoyopassAppendable();
        hoyopasses.add(hoyopass);
    }

    public void addHoyopass(String ltuid, String ltoken, HoyopassSearchPort hoyopassSearchPort) {
        this.checkHoyopassAppendable();

        Hoyopass newHoyopass = Hoyopass.builder()
                .ltuid(ltuid).ltoken(ltoken)
                .build();
        newHoyopass.fillUids(hoyopassSearchPort);

        this.addHoyopass(newHoyopass);
    }

    private void checkHoyopassAppendable() {
        if (this.isAppendable())
            throw new IllegalStateException("A User cannot have more than " + MAX_HOYOPASS_COUNT + " hoyopasses.");
    }

    public void deleteFirst() {
        this.deleteAt(0);
    }

    public void deleteLast() {
        this.deleteAt(hoyopasses.size() - 1);
    }

    public void deleteAt(int i) {
        if (0 < getCount())
            hoyopasses.remove(i);
    }

    public int getCount() {
        return hoyopasses.size();
    }

    public boolean isAppendable() {
        return MAX_HOYOPASS_COUNT <= getCount();
    }
}
