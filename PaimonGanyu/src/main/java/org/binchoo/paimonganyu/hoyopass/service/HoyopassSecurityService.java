package org.binchoo.paimonganyu.hoyopass.service;

import org.binchoo.paimonganyu.hoyopass.api.pojo.LtuidLtoken;

public interface HoyopassSecurityService {
    /**
     * secureHoyopass 문자열에서 ltuid, ltoken 값을 복구합니다.
     * @param secureHoyopass "ltuid:ltoken"을 백엔드 private key로 싸인한 문자열.
     * @return 해독된 ltuid와 ltoken
     */
    LtuidLtoken decodeSecureHoyopass(String secureHoyopass);
}
