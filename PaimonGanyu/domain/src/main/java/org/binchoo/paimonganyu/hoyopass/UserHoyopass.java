package org.binchoo.paimonganyu.hoyopass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityExceedException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
@Getter
public class UserHoyopass {

    public static final int MAX_HOYOPASS_COUNT = 2;

    private String botUserId;
    private final TreeSet<Hoyopass> hoyopasses;

    public static final class UserHoyopasBuilder {

        private String botUserId;
        private TreeSet<Hoyopass> hoyopasses;

        public UserHoyopasBuilder botUserId(String botUserId) {
            this.botUserId = botUserId;
            return this;
        }

        public UserHoyopasBuilder hoyopasses(Collection<Hoyopass> hoyopasses) {
            this.hoyopasses = new TreeSet<>(hoyopasses);
            return this;
        }

        public UserHoyopass build() {
            return new UserHoyopass(botUserId, hoyopasses);
        }
    }

    public static UserHoyopasBuilder builder() {
        return new UserHoyopasBuilder();
    }

    public UserHoyopass() {
        this.hoyopasses = new TreeSet<>();
    }

    public UserHoyopass(String botUserId) {
        this();
        this.botUserId = botUserId;
    }

    public UserHoyopass(String botUserId, Collection<Hoyopass> hoyopasses) {
        this(botUserId);
        this.hoyopasses.addAll(hoyopasses);
    }

    /**
     * @param hoyopass 통행증 객체
     * @throws IllegalStateException 최대 소지 개수 이상의 통행증을 이 유저에게 등록하려 할 경우,
     * 유저에게 중복된 통행증을 등록하려 할 경우
     */
    public void addComplete(Hoyopass hoyopass) {
        assertAppendable(hoyopass);
        this.hoyopasses.add(hoyopass);
    }

    /**
     * 이 유저 통행증 객체에 호요버스 통행증을 추가한다.
     * @param credentials 통행증 크레덴셜
     * @param uidSearchClientPort 미호요 통행증 실제 조회를 위한 검색 서비스 객체
     * @throws QuantityExceedException 유저 당 최대 소지 개수를 초과하여 통행증을 등록하려 할 경우
     * @throws DuplicationException 유저가 이미 소지한 통행증을 등록하려 할 경우
     * @throws InactiveStateException 통행증 Uid 조회 API 클라이언트에서 오류가 발생했을 경우
     */
    public void addIncomplete(HoyopassCredentials credentials, UidSearchClientPort uidSearchClientPort) {
        Hoyopass newHoyopass = Hoyopass.builder()
                .credentials(credentials)
                .build();
        fillUids(newHoyopass, uidSearchClientPort);
        addComplete(newHoyopass);
    }

    /**
     * 이 유저에게 입력받은 통행증을 등록할 수 있는지 검증한다.
     * @param hoyopass 통행증 객체
     * @throws QuantityExceedException 유저 당 최대 소지 개수를 초과하여 통행증을 등록하려 할 경우
     * @throws DuplicationException 유저가 이미 소지한 통행증을 등록하려 할 경우
     */
    private void assertAppendable(Hoyopass hoyopass) {
        checkVacancy();
        checkDuplicate(hoyopass);
    }

    /**
     * 통행증의 수량 조건 위반을 확인한다.
     * @throws QuantityExceedException 유저 당 최대 소지 개수를 초과하여 통행증을 등록하려 할 경우
     */
    private void checkVacancy() {
        if (MAX_HOYOPASS_COUNT <= this.getSize()) {
            throw new QuantityExceedException(this, "A User cannot have more than " + MAX_HOYOPASS_COUNT + " hoyopasses.");
        }
    }

    /**
     * 통행증의 유일성 위반을 확인한다.
     * @param hoyopass 유저에게 새로 등록하려는 통행증
     * @throws DuplicationException 유저가 이미 소지한 통행증을 등록하려 할 경우
     */
    private void checkDuplicate(Hoyopass hoyopass) {
        if (this.hoyopasses.stream().anyMatch(it-> it.equals(hoyopass))) {
            throw new DuplicationException(this, String.format("" +
                    "You're trying to add duplicate Hoyopass(ltuid: %s) object.", hoyopass.getLtuid()));
        }
    }

    /**
     * 주어진 통행증과 연결된 Uid 정보를 긁어온다.
     * @param newHoyopass 이 유저에게 새로 추가하려는 통행증
     * @param uidSearchClientPort Uid 검색을 위한 API 클라이언트
     * @throws InactiveStateException API 클라이언트 오류 발생시:
     * 호요버스 계정이 호요랩 비활성 상태 또는, 연결된 여행자가 없을 때
     */
    private void fillUids(Hoyopass newHoyopass, UidSearchClientPort uidSearchClientPort) {
        assertAppendable(newHoyopass);
        try {
            newHoyopass.fillUids(uidSearchClientPort);
        } catch (Exception e) {
            throw new InactiveStateException(this, e);
        }
    }

    /**
     * 모든 통행증의 {@link Uid} 리스트를 합쳐서 반환합니다.
     * 각 통행증은 생성 시점의 오른차순으로 정렬됩니다.
     * @return {@link Uid} 리스트
     */
    public List<Uid> listUids() {
        return this.hoyopasses.stream().sorted()
                .map(Hoyopass::getUids).flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * 모든 통행증의 {@code ltuid}를 합쳐서 반환합니다.
     * 각 통행증은 생성 시점의 오른차순으로 정렬됩니다.
     * @return {@link Uid} 리스트
     */
    public List<String> listLtuids() {
        return this.hoyopasses.stream().sorted()
                .map(Hoyopass::getLtuid)
                .collect(Collectors.toList());
    }

    /**
     * 지정한 통행증과 연결된 모든 {@link Uid} 리스트를 얻습니다.
     * 이들은 통행증 생성시점의 오른차순 정렬을 따릅니다.
     * @param i 번째 통행증을 지정
     * @return {@link Uid} 리스트, 잘못된 i 지정일 시 길이 0인 리스트
     */
    public List<Uid> listUids(int i) {
        if (0 <= i && i < this.getSize()) {
            Hoyopass hoyopass = (i == 0)? hoyopasses.first() : hoyopasses.last();
            return hoyopass.getUids();
        }
        return Collections.emptyList();
    }

    /**
     * @throws IndexOutOfBoundsException 잘못된 인덱스 지정일 때
     */
    public Hoyopass deleteAt(int i) {
        if (0 <= i && i < this.getSize()) {
            if (i == 0)
                return hoyopasses.pollFirst();
            else
                return hoyopasses.pollLast();
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * @return 정렬된 통행증 리스트 (복제본)
     */
    public List<Hoyopass> getHoyopasses() {
        return hoyopasses.stream().sorted()
                .collect(Collectors.toList());
    }

    public int getSize() {
        return hoyopasses.size();
    }

    public Hoyopass get(int i) {
        if (0 <= i && i < this.getSize()) {
            if (i == 0)
                return hoyopasses.first();
            else
                return hoyopasses.last();
        }
        throw new IndexOutOfBoundsException();
    }
}
