package org.binchoo.paimonganyu.hoyopass.api.webclient;

import org.binchoo.paimonganyu.hoyopass.api.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyopass.api.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.api.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyopass.api.pojo.UserGameRoles;
import org.binchoo.paimonganyu.hoyopass.api.pojo.HoyoResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Component
public class HoyolabAccountWebClient implements HoyolabAccountApi {

    private WebClient webClient;

    public HoyolabAccountWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultUriVariables(Collections.singletonMap(PARAM_GAME_BIZ, "hk4e_global"))
                .build();
    }

    @Override
    public List<UserGameRole> getUserGameRoles(LtuidLtoken ltuidLtoken) {
        ResponseEntity<HoyoResponse<UserGameRoles>> response = webClient.get()
                .uri(GET_USER_GAME_ROLE_URL)
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<UserGameRoles>>() {})
                .block();

        return response.getBody().getData().getList();
    }

    @Override
    public UserGameRole getUserGameRoleByRegion(LtuidLtoken ltuidLtoken, String region) {
        ResponseEntity<HoyoResponse<UserGameRoles>> response = webClient.get()
                .uri(GET_USER_GAME_ROLE_URL, Collections.singletonMap(PARAM_REGION, region))
                .cookie(COOKIE_LTOKEN, ltuidLtoken.getLtoken())
                .cookie(COOKIE_LTUID, ltuidLtoken.getLtuid())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<HoyoResponse<UserGameRoles>>() {})
                .block();

        return response.getBody().getData().getList().get(0);
    }
}
