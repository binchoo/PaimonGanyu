package org.binchoo.paimonganyu.hoyoapi;

import org.binchoo.paimonganyu.hoyoapi.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatars;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;

public interface HoyolabGameRecordApi extends HoyolabApi {

    /**
     * 호요랩 BBS API - OS 엔드포인트
     */
    String BASE_URL = "https://bbs-api-os.hoyolab.com/game_record/genshin/api";

    /**
     * 원신 전적 API - 보유 캐릭터 조회 - POST API
     */
    String GAME_RECORD_CHARACTER = "/character";

    /**
     * 원신 전적 API - 현재 게임 스테이터스 조회 (레진, 파견의뢰, 선계보화 등)
     */
    String GAME_RECORD_DAILYNOTE = "/dailyNote";

    /**
     *
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param uid 조회할 uid
     * @param server 조회할 서버
     * @return 해당 uid가 보유한 캐릭터 정보 목록
     */
    HoyoResponse<GenshinAvatars> getAllCharacter(LtuidLtoken ltuidLtoken, String uid, String server);

    /**
     *
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param uid 조회할 uid
     * @param server 조회할 서버
     * @param characterId 조회할 캐릭터 아이디
     * @return 해당 uid가 보유한 캐릭터 중, characterId에 매칭되는 캐릭터 정보 목록
     */
    HoyoResponse<GenshinAvatars> getCharacters(LtuidLtoken ltuidLtoken, String uid, String server, long... characterId);

    /**
     *
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param uid 조회할 uid
     * @param server 조회할 서버
     * @return 해당 uid의 일일 노트. 레진, 파견의뢰, 선계보화 등 스테이터스 정보 포함.
     */
    HoyoResponse<DailyNote> getDailyNote(LtuidLtoken ltuidLtoken, String uid, String server);
}
