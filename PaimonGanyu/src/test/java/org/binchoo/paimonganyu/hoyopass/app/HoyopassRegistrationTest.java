package org.binchoo.paimonganyu.hoyopass.app;

import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.hoyopass.domain.driving.HoyopassRegistryPort;
import org.binchoo.paimonganyu.testconfig.TestAccountConfig;
import org.binchoo.paimonganyu.testconfig.hoyopass.HoyopassIntegrationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {HoyopassIntegrationConfig.class, TestAccountConfig.class})
class HoyopassRegistrationTest {

    @Autowired
    HoyopassRegistration hoyopassRegistration;

    @Autowired
    UserHoyopassCrudPort repository;

    @Autowired
    @Qualifier("validHoyopass")
    LtuidLtoken validHoyopass;

    @Autowired
    @Qualifier("validHoyopass2")
    LtuidLtoken validHoyopass2;

    @Autowired
    @Qualifier("fakeHoyopass")
    LtuidLtoken fakeHoyopass;

    @BeforeEach
    void deleteAll() {
        repository.deleteAll();
    }

    @AfterEach
    void printAll() {
        List<UserHoyopass> allUser = repository.findAll();
        System.out.println(allUser);
    }

    @Test
    void registerSecureHoyopass() {
        //TODO: implement this test
    }

    @Test
    void givenOneHoyopass_registerHoyopass_successful() {
        registerHoyopass("10", validHoyopass);
    }

    @Test
    void givenFakeHoyopass_registerHoyopass_fails() {
        assertThrows(RetcodeException.class, ()->
                registerHoyopass("113344", fakeHoyopass));
    }

    @Test
    void givenTwoHoyopasses_registerHoyopass_successful() {
        String botUserId = "2";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        assertThat(userHoyopass.getCount()).isEqualTo(2);
    }

    @Test
    void givenDuplicateHoyopasses_registeHoyopass_fails() {
        assertThrows(IllegalStateException.class, ()-> {
            registerHoyopass("3", validHoyopass);
            registerHoyopass("3", validHoyopass);
        });
    }

    @Test
    void givenHoyopassesForManyUsers_registeHoyopass_successful() {
        for (int i = 0; i < 5; i++)
            registerHoyopass(String.valueOf(i), validHoyopass2);
    }

    @Test
    void listHoyopasses_successful() {
        String botUserId = "987654321";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        List<Hoyopass> hoyopasses = hoyopassRegistration.listHoyopasses(botUserId);

        assertThat(hoyopasses.size()).isEqualTo(2);
    }

    @Test
    void givenUnknowBotUserId_listHoyopasses_fails() {
        String botUserId = "999";

        List<Hoyopass> hoyopasses = hoyopassRegistration.listHoyopasses(botUserId);

        assertThat(hoyopasses.size()).isEqualTo(0);
    }

    @Test
    void listUids_successful() {
        String botUserId = "123456789";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        List<Uid> uids = hoyopassRegistration.listUids(botUserId);

        userHoyopass.getHoyopasses().forEach((hoyopass)-> {
            assert(uids.containsAll(hoyopass.getUids()));
        });
    }

    @Test
    void whenHoyopassDesignated_listUids_successful() {
        String botUserId = "123456789";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        List<Uid> uids = hoyopassRegistration.listUids(botUserId, 0);
        assertThat(uids.containsAll(userHoyopass.listUids(0))).isTrue();

        uids = hoyopassRegistration.listUids(botUserId, 1);
        assertThat(uids.containsAll(userHoyopass.listUids(1))).isTrue();
    }

    @Test
    void deleteHoyopass() {
        String botUserId = "1";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        hoyopassRegistration.deleteHoyopass(botUserId, 0);

        List<Hoyopass> hoyopasses = hoyopassRegistration.listHoyopasses(botUserId);
        assertThat(hoyopasses.size()).isEqualTo(1);
    }

    private UserHoyopass registerHoyopass(String botUserId, LtuidLtoken ltuidLtoken) {
        UserHoyopass userHoyopass = hoyopassRegistration.registerHoyopass(
                botUserId, ltuidLtoken.getLtuid(), ltuidLtoken.getLtoken());

        assertThat(userHoyopass.getBotUserId()).isEqualTo(botUserId);
        return userHoyopass;
    }

    private UserHoyopass registerHoyopasses(String botUserId, LtuidLtoken... ltuidLtokens) {
        UserHoyopass userHoyopass = null;
        for (LtuidLtoken ltuidLtoken : ltuidLtokens)
            userHoyopass = this.registerHoyopass(botUserId, ltuidLtoken);

        if (userHoyopass != null)
            assertThat(userHoyopass.getCount()).isEqualTo(ltuidLtokens.length);

        return userHoyopass;
    }
}