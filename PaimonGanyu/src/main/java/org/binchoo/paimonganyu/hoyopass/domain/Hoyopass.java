package org.binchoo.paimonganyu.hoyopass.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.binchoo.paimonganyu.hoyopass.domain.driven.HoyopassSearchPort;

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

    public void fillUids(HoyopassSearchPort hoyopassSearchPort) {
        List<Uid> uids = hoyopassSearchPort.findUids(this);
        this.uids = uids;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hoyopass) {
            Hoyopass other = (Hoyopass) obj;
            return this.ltuid.equals(other.getLtuid());
        }
        return false;
    }
}
