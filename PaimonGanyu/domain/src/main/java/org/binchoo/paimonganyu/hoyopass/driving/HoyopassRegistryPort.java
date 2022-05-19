package org.binchoo.paimonganyu.hoyopass.driving;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.util.List;

public interface HoyopassRegistryPort {

    /**
     * 지정된 유저가 갖는 통행증을 새로 등록한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @param credentials 통행증 크레덴셜
     * @return 저장 완료된 UserHoyopass 엔터티
     */
    UserHoyopass registerHoyopass(String botUserId, HoyopassCredentials credentials);


    /**
     * 지정된 유저가 갖는 통행증들을 조회한다. 반환된 통행증들은 생성 시점을 기준으로 오름차순으로 정렬되어 있다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @return 이 유저의 Hoyopass 리스트. 길이: [0, 2] (유저는 최대 2개 통행증 저장 가능)
     */
    List<Hoyopass> listHoyopasses(String botUserId);

    /**
     * 지정된 유저에 연결된 모든 UID들을 조회한다.
     * @param botUserId 카카오 챗봇이 유저를 식별하는 아이디
     * @return 해당 통행증이 갖는 UID들 리스트. 길이: [0, 8] (아메리카, 유럽, 아시아, 동남아) x2
     */
    List<Uid> listUids(String botUserId);

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
