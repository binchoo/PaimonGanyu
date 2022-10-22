package org.binchoo.paimonganyu.hoyoapi;

import org.binchoo.paimonganyu.hoyoapi.error.exceptions.DataNotPublicError;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError;
import org.binchoo.paimonganyu.hoyoapi.pojo.*;
import org.binchoo.paimonganyu.hoyoapi.pojo.enums.DataSwitch;

public interface GameRecordApi extends HoyolabApi {

    /**
     * 호요랩 BBS API - OS 엔드포인트
     */
    @Override
    default String getBaseUrl() {
        return "https://bbs-api-os.hoyolab.com/game_record";
    }

    /**
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param uid 조회할 uid
     * @param server 조회할 서버
     * @throws NotLoggedInError 호요랩에서 닉네임 설정을 하지 않았을 때.
     * @return 해당 uid가 보유한 캐릭터 정보 목록
     */
    HoyoResponse<GenshinAvatars> getAllAvartars(LtuidLtoken ltuidLtoken, String uid, String server);

    /**
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param uid 조회할 uid
     * @param server 조회할 서버
     * @param characterId 조회할 캐릭터 아이디
     * @return 해당 uid가 보유한 캐릭터 중, characterId에 매칭되는 캐릭터 정보 목록
     */
    HoyoResponse<GenshinAvatars> fetchAvartars(LtuidLtoken ltuidLtoken, String uid, String server, long... characterId);

    /**
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param uid 조회할 uid
     * @param server 조회할 서버
     * @throws NotLoggedInError 호요랩에서 닉네임 설정을 하지 않았을 때.
     * @throws DataNotPublicError 실시간 노트 데이터 스위치를 켜지 않았을 때.
     * @return 해당 uid의 일일 노트. 레진, 파견의뢰, 선계보화 등 스테이터스 정보 포함.
     */
    HoyoResponse<DailyNote> getDailyNote(LtuidLtoken ltuidLtoken, String uid, String server);

    /**
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param dataSwitch 제어할 데이터 스위치
     * @param on 데이터 스위치를 켜면 {@code true}, 끌 것이면 {@code false}
     * @throws NotLoggedInError 호요랩에서 닉네임 설정을 하지 않았을 때.
     * @return 해당 통행증 계정의
     */
    HoyoResponse<ChangeDataSwitchResult> changeDataSwitch(LtuidLtoken ltuidLtoken, DataSwitch dataSwitch, boolean on);
}
