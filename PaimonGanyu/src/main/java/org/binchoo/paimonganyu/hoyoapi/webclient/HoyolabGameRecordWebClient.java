package org.binchoo.paimonganyu.hoyoapi.webclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.ds.BasicDsGenerator;
import org.binchoo.paimonganyu.hoyoapi.ds.DsGenerator;
import org.binchoo.paimonganyu.hoyoapi.ds.DsHeaderGenerator;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatars;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.response.HoyoResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.binchoo.paimonganyu.hoyoapi.constant.HoyoConstant.*;

public class HoyolabGameRecordWebClient implements HoyolabGameRecordApi {

    /**
     * 원신 전적 API - 보유 캐릭터 조회 - POST API
     */
    private final String GAME_RECORD_CHARACTER = "/character";

    /**
     * 원신 전적 API - 현재 게임 스테이터스 조회 (레진, 파견의뢰, 선계보화 등)
     */
    private final String GAME_RECORD_DAILYNOTE = "/dailyNote";

    private WebClient webClient;
    private DsHeaderGenerator dsHeaderGenerator;

    public HoyolabGameRecordWebClient() {
        this(new BasicDsGenerator());
    }

    public HoyolabGameRecordWebClient(DsGenerator dsGenerator) {
        this.webClient = WebClient.create(getBaseUrl());
        this.dsHeaderGenerator = DsHeaderGenerator.builder()
                .dsGenerator(dsGenerator).build();
    }

    @Override
    public HoyoResponse<GenshinAvatars> getAllAvartar(LtuidLtoken ltuidLtoken, String uid, String server) {
        ResponseEntity<HoyoResponse<GenshinAvatars>> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(GAME_RECORD_CHARACTER)
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
    @Deprecated
    @Override
    public HoyoResponse<GenshinAvatars> fetchAvartars(LtuidLtoken ltuidLtoken, String uid, String server, long... characterId) {
        ResponseEntity<HoyoResponse<GenshinAvatars>> response = webClient.post()
                .uri(GAME_RECORD_CHARACTER)
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
    @Deprecated
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
                        .path(GAME_RECORD_DAILYNOTE)
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
}
