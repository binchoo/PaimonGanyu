package org.binchoo.paimonganyu.hoyopass.domain.driven;

import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;

import java.util.List;
import java.util.Optional;

public interface HoyopassRepositoryPort {

    /**
     * 레포지토리에서 botUserId 챗봇 유저가 갖고 있는 모든 미호요 통행증을 가져옵니다.
     * @param botUserId
     * @return 해당 유저가 갖고 있는 {@link Hoyopass} 리스트
     */
    List<Hoyopass> findByBotUserId(String botUserId);

    /**
     * 레포지토리에서 해당 ltuid를 갖는 미호요 통행증을 가져옵니다.
     * @param ltuid
     * @return 해당 유저가 갖고 있는 {@link Hoyopass}의 {@link Optional}
     */
    Optional<Hoyopass> findByLtuid(String ltuid);

    /**
     * 레포지토리에 {@link Hoyopass}를 저장합니다.
     * @param hoyopass
     * @return 저장 완료된 {@link Hoyopass} 객체
     */
    Hoyopass save(Hoyopass hoyopass);

    /**
     * 레포지토리에서 {@link Hoyopass}를 삭제합니다.
     * @param hoyopass
     */
    void delete(Hoyopass hoyopass);
}
