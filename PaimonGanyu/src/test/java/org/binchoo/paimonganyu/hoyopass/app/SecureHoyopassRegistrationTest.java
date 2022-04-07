package org.binchoo.paimonganyu.hoyopass.app;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.domain.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.testconfig.TestAccountConfig;
import org.binchoo.paimonganyu.testconfig.hoyopass.HoyopassIntegrationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {HoyopassIntegrationConfig.class, TestAccountConfig.class})
class SecureHoyopassRegistrationTest extends HoyopassRegistrationTest {

    @Autowired
    SecureHoyopassRegistration hoyopassRegistration;

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
    @Override
    void deleteAll() {
        super.deleteAll();
    }

    @AfterEach
    @Override
    void printAll() {
        super.printAll();
    }

    @Test
    @Override
    void registerSecureHoyopass() {
        super.registerSecureHoyopass();
    }

    @Test
    @Override
    void givenOneHoyopass_registerHoyopass_successful() {
        super.givenOneHoyopass_registerHoyopass_successful();
    }

    @Test
    @Override
    void givenFakeHoyopass_registerHoyopass_fails() {
        super.givenFakeHoyopass_registerHoyopass_fails();
    }

    @Test
    @Override
    void givenTwoHoyopasses_registerHoyopass_successful() {
        super.givenTwoHoyopasses_registerHoyopass_successful();
    }

    @Test
    @Override
    void givenDuplicateHoyopasses_registeHoyopass_fails() {
        super.givenDuplicateHoyopasses_registeHoyopass_fails();
    }

    @Test
    @Override
    void givenHoyopassesForManyUsers_registeHoyopass_successful() {
        super.givenHoyopassesForManyUsers_registeHoyopass_successful();
    }

    @Test
    @Override
    void listHoyopasses_successful() {
        super.listHoyopasses_successful();
    }

    @Test
    @Override
    void givenUnknowBotUserId_listHoyopasses_fails() {
        super.givenUnknowBotUserId_listHoyopasses_fails();
    }

    @Test
    @Override
    void listUids_successful() {
        super.listUids_successful();
    }

    @Test
    @Override
    void whenHoyopassDesignated_listUids_successful() {
        super.whenHoyopassDesignated_listUids_successful();
    }

    @Test
    @Override
    void deleteHoyopass() {
        super.deleteHoyopass();
    }
}