package org.binchoo.paimonganyu.hoyopass;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.binchoo.paimonganyu.hoyopass.driven.HoyopassSearchClientPort;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Builder(toBuilder = true)
public class Hoyopass {

    /**
     * 통행증 고유의 ltuid
     */
    private String ltuid;

    /**
     * ltuid에 대응하는 ltoken
     */
    private String ltoken;

    /**
     * 이 통행증에 연결된 UID들 리스트
     */
    private List<Uid> uids;

    /**
     * 해당 통행증 객체가 생성된 시간
     */
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

    /**
     * @param hoyopassSearchClientPort UID 색인 서비스 객체
     * @throws IllegalArgumentException 이 통행증으로 UID를 색인하는 데 실패했을 경우
     */
    public void fillUids(HoyopassSearchClientPort hoyopassSearchClientPort) {
        List<Uid> findResult = hoyopassSearchClientPort.findUids(this);
        this.uids = findResult;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hoyopass) {
            Hoyopass other = (Hoyopass) obj;
            return ltuid.equals(other.getLtuid());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ltuid.hashCode();
    }
}
