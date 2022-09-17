package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRoles;
import org.binchoo.paimonganyu.hoyoapi.pojo.enums.HoyoGame;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.binchoo.paimonganyu.hoyoapi.HoyolabConstant.*;

@Component
public class HoyolabAccountWebClient implements HoyolabAccountApi {

    /**
     * <p> 해당 유저의 원신 게임 롤을 모두 조회하는 엔드포인트 - GET API
     * <p> (ltuid,ltoken -> uid 매핑에 이용할 수 있다)
     */
    private static final String GET_USER_GAME_ROLE_URL = "/getUserGameRolesByLtoken";

    private WebClient webClient;

    public HoyolabAccountWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(getBaseUrl())
                .build();
    }

    /**
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError 유효하지 않은 통행증이거나
     * HoYoLab에 닉네임 등록하지 않은 통행증일 때
     */
    @Override
    public HoyoResponse<UserGameRoles> getUserGameRoles(LtuidLtoken ltuidLtoken) {
        ResponseEntity<HoyoResponse<UserGameRoles>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(GET_USER_GAME_ROLE_URL)
                        .queryParam(PARAM_GAME_BIZ, HoyoGame.GENSHIN_IMPACT.gameBizString())
                        .build())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<UserGameRoles>>() {})
                .block();

        return response.getBody();
    }

    /**
     * @throws org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError 유효하지 않은 통행증이거나
     * HoYoLab에 닉네임 등록하지 않은 통행증일 때
     */
    @Override
    public HoyoResponse<UserGameRoles> getUserGameRoleByRegion(LtuidLtoken ltuidLtoken, String region) {
        ResponseEntity<HoyoResponse<UserGameRoles>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GET_USER_GAME_ROLE_URL)
                        .queryParam(PARAM_REGION, region)
                        .queryParam(PARAM_GAME_BIZ, HoyoGame.GENSHIN_IMPACT.gameBizString())
                        .build())
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<UserGameRoles>>() {})
                .block();

        return response.getBody();
    }
}
