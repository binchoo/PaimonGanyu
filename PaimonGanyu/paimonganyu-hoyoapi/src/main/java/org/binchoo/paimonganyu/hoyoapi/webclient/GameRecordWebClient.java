package org.binchoo.paimonganyu.hoyoapi.webclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.binchoo.paimonganyu.hoyoapi.GameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.*;
import org.binchoo.paimonganyu.hoyoapi.pojo.enums.DataSwitch;
import org.binchoo.paimonganyu.hoyoapi.pojo.enums.HoyoGame;
import org.binchoo.paimonganyu.hoyoapi.tool.DsHeaderGenerator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.binchoo.paimonganyu.hoyoapi.HoyolabConstant.*;

@Component
public class GameRecordWebClient implements GameRecordApi {

    /**
     * 원신 전적 API - 보유 캐릭터 조회 - POST API
     */
    private static final String API_CHARACTER = "/genshin/api/character";
    /**
     * 원신 전적 API - 현재 게임 스테이터스 조회 (레진, 파견의뢰, 선계보화 등)
     */
    private static final String API_DAILYNOTE = "/genshin/api/dailyNote";
    /**
     * 원신 전적 API - 데이터 스위치 조작 (전투 연대기, 캐릭터 상세, 실시간 일일 노트)
     */
    private static final String API_CHANGE_DATASWITCH = "/card/wapi/changeDataSwitch";

    private final DsHeaderGenerator dsHeaderGenerator;

    private WebClient webClient;

    public GameRecordWebClient(DsHeaderGenerator dsHeaderGenerator) {
        this.webClient = WebClient.create(getBaseUrl());
        this.dsHeaderGenerator = dsHeaderGenerator;
    }

    @Override
    public HoyoResponse<GenshinAvatars> getAllAvartars(LtuidLtoken ltuidLtoken, String uid, String server) {
        ResponseEntity<HoyoResponse<GenshinAvatars>> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(API_CHARACTER)
                        .queryParam(PARAM_ROLE_ID, uid)
                        .queryParam(PARAM_SERVER, server)
                        .build())
                .headers(headers-> headers
                        .addAll(dsHeaderGenerator.generateDsHeader()))
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<GenshinAvatars>>() {})
                .block();

        return response.getBody();
    }

    /**
     * @deprecated 지정한 characterId 뿐만 아닌 모든 캐릭터 정보를 불러오는 이슈 있음
     */
    @Deprecated(forRemoval = false)
    @Override
    public HoyoResponse<GenshinAvatars> fetchAvartars(LtuidLtoken ltuidLtoken, String uid, String server, long... characterId) {
        ResponseEntity<HoyoResponse<GenshinAvatars>> response = webClient.post()
                .uri(API_CHARACTER)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers-> headers
                        .addAll(dsHeaderGenerator.generateDsHeader()))
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .bodyValue(CharacterPayload.builder()
                        .roleId(uid)
                        .server(server)
                        .characterIds(LongStream.of(characterId)
                                .boxed()
                                .collect(Collectors.toList()))
                        .build())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<GenshinAvatars>>() {})
                .block();

        return response.getBody();
    }

    /**
     * @deprecated fetchAvartars(LtuidLtoken, String, String, long...)가 제외되었음.
     */
    @Deprecated(forRemoval = false)
    @Builder
    private static final class CharacterPayload {

        @JsonProperty("role_id")
        String roleId;

        @JsonProperty("server")
        String server;

        @JsonProperty("character_ids")
        List<Long> characterIds;
    }

    @Override
    public HoyoResponse<DailyNote> getDailyNote(LtuidLtoken ltuidLtoken, String uid, String server) {
        ResponseEntity<HoyoResponse<DailyNote>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_DAILYNOTE)
                        .queryParam(PARAM_ROLE_ID, uid)
                        .queryParam(PARAM_SERVER, server)
                        .build())
                .headers(headers-> headers
                        .addAll(dsHeaderGenerator.generateDsHeader()))
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<DailyNote>>() {})
                .block();

        return response.getBody();
    }

    @Override
    public HoyoResponse<ChangeDataSwitchResult> changeDataSwitch(LtuidLtoken ltuidLtoken, DataSwitch dataSwitch, boolean turnOn) {
        ResponseEntity<HoyoResponse<ChangeDataSwitchResult>> response = webClient.post()
                .uri(API_CHANGE_DATASWITCH)
                .headers(headers-> headers
                        .addAll(dsHeaderGenerator.generateDsHeader()))
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .bodyValue(DataSwitchPayload.builder()
                        .gameId(HoyoGame.GENSHIN_IMPACT.gameId())
                        .switchId(dataSwitch.switchId())
                        .isPublic(turnOn)
                        .build())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<ChangeDataSwitchResult>>() {})
                .block();

        return response.getBody();
    }

    @Builder
    private static final class DataSwitchPayload {

        @JsonProperty("game_id")
        private int gameId;

        @JsonProperty("switch_id")
        private int switchId;

        @JsonProperty("is_public")
        private boolean isPublic;
    }
}
