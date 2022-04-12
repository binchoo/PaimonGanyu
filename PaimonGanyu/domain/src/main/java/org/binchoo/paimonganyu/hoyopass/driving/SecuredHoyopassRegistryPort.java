package org.binchoo.paimonganyu.hoyopass.domain.driving;

import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;

/**
 * packageName : org.binchoo.paimonganyu.hoyopass.domain.driving
 * fileName : HoyopassSecurityLayer
 * author : jbinchoo
 * date : 2022-04-07
 * description :
 */
public interface SecuredHoyopassRegistryPort extends HoyopassRegistryPort {

    /**
     * 지정된 유저가 갖는 통행증을 새로 등록한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @param secureHoyopassString "ltuid:ltoken"을 백엔드 private key로 싸인한 문자열
     * @return 저장 완료된 UserHoyopass 엔터티
     */
    UserHoyopass registerHoyopass(String botUserId, String secureHoyopassString);
}
