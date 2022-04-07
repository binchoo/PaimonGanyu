package org.binchoo.paimonganyu.hoyopass.domain.driven;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface SingingKeyManagerPort {

    /**
     * @return 통행증 암호화에 이용되는 공개키
     */
    PublicKey getPublicKey();

    /**
     * @return 통행증 복호화에 이용되는 사설키
     */
    PrivateKey getPrivateKey();
}
