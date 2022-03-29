package org.binchoo.paimonganyu.hoyopass.domain.driving;

import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;

import java.util.List;

public interface HoyopassSearchPort {

    /**
     * 주어진 {@link Hoyopass} 객체와 연관된 {@link org.binchoo.paimonganyu.hoyopass.domain.Uid}를 색인하여 채워넣습니다.
     * @param hoyopass
     * @return {@link org.binchoo.paimonganyu.hoyopass.domain.Uid} 값이 채워진 {@link Hoyopass} 객체
     */
    List<Uid> findUids(Hoyopass hoyopass);
}
