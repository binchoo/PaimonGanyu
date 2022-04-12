package org.binchoo.paimonganyu.hoyopass.domain.driven;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface SigningKeyManagerPort {

    /**
     * @return 통행증 암호화에 이용되는 공개키
     */
    PublicKey getPublicKey();

    /**
     * @return 통행증 복호화에 이용되는 사설키
     */
    PrivateKey getPrivateKey();

    /**
     * {@link SigningKeyManagerPort}가 암복호화에 이용하는 키 알고리즘을 반환합니다.
     * @return 키 알고리즘. 반환 종류는 {@link java.security.spec.KeySpec} 명세를 참조한다.
     */
    String getAlgorithm();
}
