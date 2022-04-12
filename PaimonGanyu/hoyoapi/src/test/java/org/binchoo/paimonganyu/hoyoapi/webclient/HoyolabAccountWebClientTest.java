package org.binchoo.paimonganyu.hoyoapi.webclient;

import org.binchoo.paimonganyu.hoyoapi.pojo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {TestAccountConfig.class})
class HoyolabAccountWebClientTest {

    HoyolabAccountWebClient accountApi = new HoyolabAccountWebClient();

    @Autowired
    @Qualifier("validHoyopass")
    LtuidLtoken validAccount; // this account has Asia & Usa uids

    @Autowired
    @Qualifier("fakeHoyopass")
    LtuidLtoken fakeAccount;

    @Test
    void givenValidAccount_getUserGameRoles_successful() {
        HoyoResponse<UserGameRoles> response = accountApi.getUserGameRoles(validAccount);
        List<UserGameRole> userRoles = response.getData().getList();

        boolean hasAsiaAndUsaRole = 2 == userRoles.stream().filter(ugr ->
                ugr.getRegion().equals(UidRegion.OS_ASIA.lowercase())
                        || ugr.getRegion().equals(UidRegion.OS_USA.lowercase())).count();

        assertThat(hasAsiaAndUsaRole).isTrue();
    }

    @Test
    void givenFakeAccount_getUserGameRoles_returnErrorResponse() {
        HoyoResponse<UserGameRoles> response = accountApi.getUserGameRoles(fakeAccount);

        assertThat(response.getRetcode()).isNotEqualTo(0);
        assertThat(response.getData()).isNull();
    }

    @Test
    void givenAsiaAccount_getUserGameRoles_hasAsiaUid() {
        HoyoResponse<UserGameRoles> response = accountApi.getUserGameRoles(validAccount);
        List<UserGameRole> userRoles = response.getData().getList();

        boolean hasAsiaUid = userRoles.stream().anyMatch(ugr ->
                ugr.getRegion().equals(UidRegion.OS_ASIA.lowercase()));

        assertThat(hasAsiaUid).isTrue();
    }

    @Test
    void givenUsaAccount_getUserGameRoles_hasUsaUid() {
        HoyoResponse<UserGameRoles> response = accountApi.getUserGameRoles(validAccount);
        List<UserGameRole> userRoles = response.getData().getList();

        boolean hasUsaUid = userRoles.stream().anyMatch(ugr ->
                ugr.getRegion().equals(UidRegion.OS_USA.lowercase()));

        assertThat(hasUsaUid).isTrue();
    }

    @Test
    void whenSearchingAsia_getUserGameRoleByRegion_onlyReturnsAsiaUid() {
        HoyoResponse<UserGameRoles> response = accountApi.getUserGameRoleByRegion(validAccount,
                UidRegion.OS_ASIA.lowercase());
        List<UserGameRole> userRoles = response.getData().getList();

        System.out.println(userRoles);
        assertThat(userRoles.size()).isEqualTo(1);
        assertThat(UidRegion.fromString(userRoles.get(0).getRegion())).isEqualTo(UidRegion.OS_ASIA);
    }

    @Test
    void whenSearchingUsa_getUserGameRoleByRegion_onlyReturnsUsaUid() {
        HoyoResponse<UserGameRoles> response = accountApi.getUserGameRoleByRegion(validAccount,
                UidRegion.OS_USA.lowercase());
        List<UserGameRole> userRoles = response.getData().getList();

        assertThat(userRoles.size()).isEqualTo(1);
        assertThat(UidRegion.fromString(userRoles.get(0).getRegion())).isEqualTo(UidRegion.OS_USA);
    }
}