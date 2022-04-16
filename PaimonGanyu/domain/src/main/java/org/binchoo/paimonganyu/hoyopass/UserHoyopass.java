package org.binchoo.paimonganyu.hoyopass;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.binchoo.paimonganyu.hoyopass.driven.HoyopassSearchClientPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Builder
@Getter
public class UserHoyopass {

    private static final int MAX_HOYOPASS_COUNT = 2;

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
        this(botUserId);
        this.hoyopasses.addAll(hoyopasses);
    }

    /**
     * @throws IllegalStateException 최대 소지 개수 이상의 통행증을 이 유저에게 등록하려 할 경우,
     * 유저에게 중복된 통행증을 등록하려 할 경우
     * @param hoyopass 통행증 객체
     */
    public void addVerifiedHoyopass(Hoyopass hoyopass) {
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

    /**
     * @throws IllegalStateException 최대 소지 개수 이상의 통행증을 이 유저에게 등록하려 할 경우,
     * 유저에게 중복된 통행증을 등록하려 할 경우
     * @throws IllegalArgumentException 입력한 값이 실제 미호요와 상호작용 할 수 있는 통행증이 아닐 경우
     * @param ltuid 통행증 ID
     * @param ltoken 통행증 크레덴셜 토큰
     * @param hoyopassSearchClientPort 미호요 통행증 실제 조회를 위한 검색 서비스 객체
     */
    public void addUnverifiedHoyopass(String ltuid, String ltoken, HoyopassSearchClientPort hoyopassSearchClientPort) {
        Hoyopass newHoyopass = Hoyopass.builder()
                .ltuid(ltuid).ltoken(ltoken).build();

        this.addVerifiedHoyopass(newHoyopass);
        newHoyopass.fillUids(hoyopassSearchClientPort);
    }

    public List<Uid> listUids() {
        return this.hoyopasses.stream().map(Hoyopass::getUids)
                .flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * 지정한 통행증과 연결된 모든 {@link Uid} 리스트를 얻습니다.
     * @param i 번째 통행증을 지정
     * @return {@link Uid} 리스트, 잘못된 i 지정일 시 길이 0인 리스트
     */
    public List<Uid> listUids(int i) {
        if (0 <= i && i < this.hoyopasses.size())
            return this.hoyopasses.get(i).getUids();
        return Collections.emptyList();
    }

    /**
     * @throws IndexOutOfBoundsException – i < 0 || i >= getCount() 일 때
     */
    public Hoyopass deleteAt(int i) {
        return hoyopasses.remove(i);
    }

    public int getCount() {
        return hoyopasses.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserHoyopass) {
            UserHoyopass other = (UserHoyopass) obj;
            return botUserId.equals(other.botUserId) && hoyopasses.equals(other.hoyopasses);
        }
        return false;
    }
}
