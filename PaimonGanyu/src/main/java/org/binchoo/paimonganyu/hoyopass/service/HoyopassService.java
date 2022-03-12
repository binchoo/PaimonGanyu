package org.binchoo.paimonganyu.hoyopass.service;

import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.entity.Uid;

import java.util.List;

public interface HoyopassService {

    /**
     * 지정된 유저가 갖는 통행증을 새로 등록한다.
     * @param userId 카카오 챗봇이 유저를 식별하는 아이디
     * @param secureHoyopass "ltuid:ltoken"을 백엔드 private key로 싸인한 문자열
     * @return 저장 완료된 Hoyopass 엔터티
     */
    Hoyopass createHoyopass(String userId, String secureHoyopass);

    /**
     * 지정된 유저가 갖는 통행증을 새로 등록한다.
     * @param userId 카카오 챗봇이 유저를 식별하는 아이디
     * @param ltuid 통행증 ltuid
     * @param ltoken 통행증 ltoken
     * @return 저장 완료된 Hoyopass 엔터티
     */
    Hoyopass createHoyopass(String userId, String ltuid, String ltoken);


    /**
     * 지정된 유저가 갖는 통행증들을 조회한다.
     * @param userId 카카오 챗봇이 유저를 식별하는 아이디
     * @return 이 유저의 Hoyopass 리스트. 길이: [0, 2] (유저는 최대 2개 통행증 저장 가능)
     */
    List<Hoyopass> getHoyopassList(String userId);

    /**
     * 지정된 유저 통행증에 연결된 UID들을 조회한다.
     * @param userId 카카오 챗봇이 유저를 식별하는 아이디
     * @param hoyopassId Hoyopass 엔터티의 아이디
     * @return 해당 통행증이 갖는 UID들 리스트. 길이: [0, 4] (아메리카, 유럽, 아시아, 동남아)
     */
    List<Uid> getUidList(String userId, String hoyopassId);

    /**
     * 지정된 유저로부터 지정된 통행증을 삭제한다.
     * @param userId 카카오 챗봇이 유저를 식별하는 아이디
     * @param hoyopassId Hyopass 엔터티의 아이디
     */
    void deleteHoyopass(String userId, String hoyopassId);
}
