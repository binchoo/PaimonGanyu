package org.binchoo.paimonganyu.hoyopass.driven;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;

import java.util.List;

public interface UidSearchClientPort {

    /**
     * 주어진 {@link Hoyopass} 객체와 연관된 {@link Uid}를 색인하여 채워넣습니다.
     * @param hoyopass 통행증 객체
     * @throws IllegalArgumentException 주어진 통행증으로 UID를 색인할 수 없을 경우
     * @return {@link Uid} 값이 채워진 {@link Hoyopass} 객체
     */
    List<Uid> findUids(Hoyopass hoyopass);
}
