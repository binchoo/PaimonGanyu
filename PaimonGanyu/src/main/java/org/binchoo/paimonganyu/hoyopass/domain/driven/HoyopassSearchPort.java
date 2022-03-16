package org.binchoo.paimonganyu.hoyopass.domain.driven;

import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;

public interface HoyopassSearchPort {

    /**
     * 주어진 {@link Hoyopass} 객체와 연관된 UID 객체를 채워넣습니다.
     * @param hoyopass
     * @return {@link org.binchoo.paimonganyu.hoyopass.domain.Uid} 값이 채워진 {@link Hoyopass} 객체
     */
    Hoyopass fillUids(Hoyopass hoyopass);
}
