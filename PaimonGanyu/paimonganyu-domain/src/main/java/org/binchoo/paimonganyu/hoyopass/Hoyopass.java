package org.binchoo.paimonganyu.hoyopass;

import lombok.*;
import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Hoyopass implements Comparable<Hoyopass> {

    private HoyopassCredentials credentials;

    /**
     * 이 통행증에 연결된 UID들 리스트
     */
    @Singular("addUid")
    private List<Uid> uids;

    private final Map<String, Uid> uidCache = new WeakHashMap<>(4);

    /**
     * 해당 통행증 객체가 생성된 시간
     */
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

    /**
     * @param uidSearchClientPort UID 색인 서비스 객체
     * @throws IllegalArgumentException 이 통행증으로 UID를 색인하는 데 실패했을 경우
     */
    public void fillUids(UidSearchClientPort uidSearchClientPort) {
        this.uids = uidSearchClientPort.findUids(this);
    }

    public String getLtuid() {
        return credentials.getLtuid();
    }

    public String getLtoken() {
        return credentials.getLtoken();
    }

    public String getCookieToken() {
        return credentials.getCookieToken();
    }

    public int size() {
        return uids.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hoyopass) {
            Hoyopass other = (Hoyopass) obj;
            return credentials.equals(other.getCredentials());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return credentials.hashCode();
    }

    @Override
    public int compareTo(Hoyopass o) {
        int ascendingCreateAt = createAt.compareTo(o.createAt);
        if (ascendingCreateAt == 0) {
            int ascendingLtuid = this.getLtuid().compareTo(o.getLtuid());
            return ascendingLtuid;
        }
        return ascendingCreateAt;
    }

    public boolean contains(String uid) {
        initCache();
        return uidCache.containsKey(uid);
    }

    public Uid remove(String uidString) {
        initCache();
        Uid remove = uidCache.get(uidString);
        if (remove != null) {
            this.uids = uids.stream().filter(uid-> !remove.equals(uid)).collect(Collectors.toList());
            uidCache.remove(uidString);
            return remove;
        }
        return null;
    }

    private void initCache() {
        if (uidCache.isEmpty()) {
            for (Uid uid : uids) {
                String uidString = uid.getUidString();
                uidCache.put(uidString, uid);
            }
        }
    }
}
