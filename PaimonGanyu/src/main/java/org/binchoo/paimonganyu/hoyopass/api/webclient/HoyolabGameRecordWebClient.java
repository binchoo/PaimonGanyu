package org.binchoo.paimonganyu.hoyopass.api.webclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.binchoo.paimonganyu.hoyopass.api.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyopass.api.ds.BasicDsGenerator;
import org.binchoo.paimonganyu.hoyopass.api.ds.DsGenerator;
import org.binchoo.paimonganyu.hoyopass.api.pojo.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class HoyolabGameRecordWebClient implements HoyolabGameRecordApi {

    private WebClient webClient;
    private DsGenerator dsGenerator;

    public HoyolabGameRecordWebClient() {
        this.webClient = WebClient.create(BASE_URL);
        this.dsGenerator = BasicDsGenerator.basic();
    }

    @Override
    public List<GenshinAvatar> getAllCharacter(LtuidLtoken ltuidLtoken, String uid, String server) {
        ResponseEntity<HoyoResponse<GenshinAvatars>> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(GAME_RECORD_CHARACTER)
                        .queryParam(PARAM_ROLE_ID, uid)
                        .queryParam(PARAM_SERVER, server)
                        .build())
                .headers(headers-> headers
                        .addAll(dsGenerator.generateDsHeader()))
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<GenshinAvatars>>() {})
                .block();

        return response.getBody().getData().getAvatars();
    }

    /**
     *
     * @param ltuidLtoken 유저 통행증 쿠키
     * @param uid 조회할 uid
     * @param server 조회할 서버
     * @param characterId 조회할 캐릭터 아이디
     * @deprecated 지정한 characterId 뿐만 아닌 모든 캐릭터 정보를 불러오는 이슈 있음
     * @return 해당 uid가 보유한 캐릭터 중, characterId에 매칭되는 캐릭터 정보 목록
     */
    @Deprecated
    @Override
    public List<GenshinAvatar> getCharacters(LtuidLtoken ltuidLtoken, String uid, String server, long... characterId) {
        ResponseEntity<HoyoResponse<GenshinAvatars>> response = webClient.post()
                .uri(GAME_RECORD_CHARACTER)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers-> headers
                        .addAll(dsGenerator.generateDsHeader()))
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

        return response.getBody().getData().getAvatars();
    }

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
    public DailyNote getDailyNote(LtuidLtoken ltuidLtoken, String uid, String server) {
        ResponseEntity<HoyoResponse<DailyNote>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GAME_RECORD_DAILYNOTE)
                        .queryParam(PARAM_ROLE_ID, uid)
                        .queryParam(PARAM_SERVER, server)
                        .build())
                .headers(headers-> headers
                        .addAll(dsGenerator.generateDsHeader()))
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<DailyNote>>() {})
                .block();

        return response.getBody().getData();
    }
}
