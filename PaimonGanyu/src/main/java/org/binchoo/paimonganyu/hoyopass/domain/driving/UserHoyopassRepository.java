package org.binchoo.paimonganyu.hoyopass.domain.driving;

import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;

import java.util.Optional;

public interface UserHoyopassRepository {

    /**
     * 카카오 챗봇 유저의 정보입니다. 연관된 통행증 정보를 갖습니다.
     * @param botUserId 카카오 챗봇이 식별한 유저 고유 아이디
     * @return {@link UserHoyopass} 옵셔널
     */
    Optional<UserHoyopass> findByBotUserId(String botUserId);

    /**
     * 카카오 챗봇 유저의 통행증 정보를 저장소에 저장합니다.
     * @param entity 카카오 챗봇 유저의 통행증 정보
     * @return 저장 완료한 통행증 정보
     */
    UserHoyopass save(UserHoyopass entity);

    /**
     * 카카오 챗봇 유저의 모든 통행증 정보를 삭제합니다.
     * @param userHoyopass 카카오 챗봇이 식별한 유저 고유 아이디
     */
    void delete(UserHoyopass userHoyopass);

    /**
     * 카카옷 챗봇 유저가 등록한 통행증이 있는지 확인합니다.
     * @param botUserId 카카오 챗봇이 식별한 유저 고유 아이디
     * @return 이 챗봇 유저의 통행증 등록 여부
     */
    boolean existsByBotUserId(String botUserId);
}
