package org.binchoo.paimonganyu.hoyopass.domain.inport;

import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;

import java.util.List;

public interface HoyopassChatbotService {

    /**
     * 지정된 유저가 갖는 통행증을 새로 등록한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @param secureHoyopass "ltuid:ltoken"을 백엔드 private key로 싸인한 문자열
     * @return 저장 완료된 Hoyopass 엔터티
     */
    Hoyopass registerSecureHoyopass(String botUserId, String secureHoyopass);

    /**
     * 지정된 유저가 갖는 통행증을 새로 등록한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @param ltuid 통행증 ltuid
     * @param ltoken 통행증 ltoken
     * @return 저장 완료된 Hoyopass 엔터티
     */
    Hoyopass registerHoyopass(String botUserId, String ltuid, String ltoken);


    /**
     * 지정된 유저가 갖는 통행증들을 조회한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @return 이 유저의 Hoyopass 리스트. 길이: [0, 2] (유저는 최대 2개 통행증 저장 가능)
     */
    List<Hoyopass> listHoyopasses(String botUserId);

    /**
     * 지정된 유저에 연결된 모든 UID들을 조회한다.
     * @param userId 카카오 챗봇이 유저를 식별하는 아이디
     * @return 해당 통행증이 갖는 UID들 리스트. 길이: [0, 8] (아메리카, 유럽, 아시아, 동남아) x2
     */
    List<Uid> listUids(String userId);

    /**
     * 지정된 유저 통행증에 연결된 UID들을 조회한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @param order 조회 대상 통행증 객체가 생성시점 순으로 오름차순 정렬될 때의 순서
     * @return 해당 통행증이 갖는 UID들 리스트. 길이: [0, 4] (아메리카, 유럽, 아시아, 동남아)
     */
    List<Uid> listUids(String botUserId, int order);

    /**
     * 지정된 유저의 order번째 통행증을 삭제한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @param order 삭제할 통행증 객체가 생성시점 순으로 오름차순 정렬될 때의 순서
     */
    void deleteHoyopass(String botUserId, int order);
}
