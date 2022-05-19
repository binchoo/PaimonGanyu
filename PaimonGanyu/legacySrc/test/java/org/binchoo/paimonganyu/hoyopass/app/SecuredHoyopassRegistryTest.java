package org.binchoo.paimonganyu.hoyopass.app;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.SigningKeyManagerPort;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.testconfig.TestAccountConfig;
import org.binchoo.paimonganyu.testconfig.hoyopass.HoyopassIntegrationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {HoyopassIntegrationConfig.class, TestAccountConfig.class})
class SecuredHoyopassRegistryTest {

    @Autowired
    SecuredHoyopassRegistry hoyopassRegistry;

    @Autowired
    SigningKeyManagerPort keyManager;

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
        String botUserId = RandomString.make();

        hoyopassRegistry.registerHoyopass(botUserId, getSecureHoyopass(validHoyopass));

        UserHoyopass userHoyopass = repository.findByBotUserId(botUserId).orElseThrow(RuntimeException::new);
        assertThat(userHoyopass.getBotUserId()).isEqualTo(botUserId);
        assertThat(userHoyopass.getCount()).isEqualTo(1);
        assertThat(userHoyopass.getHoyopasses()).map(Hoyopass::getLtuid)
                .anyMatch(ltuid-> ltuid.equals(validHoyopass.getLtuid()));
        assertThat(userHoyopass.getHoyopasses()).map(Hoyopass::getLtoken)
                .anyMatch(ltoken-> ltoken.equals(validHoyopass.getLtoken()));
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

        List<Hoyopass> hoyopasses = hoyopassRegistry.listHoyopasses(botUserId);

        assertThat(hoyopasses.size()).isEqualTo(2);
    }

    @Test
    void givenUnknowBotUserId_listHoyopasses_fails() {
        String botUserId = "999";

        List<Hoyopass> hoyopasses = hoyopassRegistry.listHoyopasses(botUserId);

        assertThat(hoyopasses.size()).isEqualTo(0);
    }

    @Test
    void listUids_successful() {
        String botUserId = "123456789";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        List<Uid> uids = hoyopassRegistry.listUids(botUserId);

        userHoyopass.getHoyopasses().forEach((hoyopass)-> {
            assert(uids.containsAll(hoyopass.getUids()));
        });
    }

    @Test
    void whenHoyopassDesignated_listUids_successful() {
        String botUserId = "123456789";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        List<Uid> uids = hoyopassRegistry.listUids(botUserId, 0);
        assertThat(uids.containsAll(userHoyopass.listUids(0))).isTrue();

        uids = hoyopassRegistry.listUids(botUserId, 1);
        assertThat(uids.containsAll(userHoyopass.listUids(1))).isTrue();
    }

    @Test
    void deleteHoyopass() {
        String botUserId = "1";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, validHoyopass, validHoyopass2);

        hoyopassRegistry.deleteHoyopass(botUserId, 0);

        List<Hoyopass> hoyopasses = hoyopassRegistry.listHoyopasses(botUserId);
        assertThat(hoyopasses.size()).isEqualTo(1);
    }

    private UserHoyopass registerHoyopass(String botUserId, LtuidLtoken ltuidLtoken) {
        UserHoyopass userHoyopass = hoyopassRegistry.registerHoyopass(
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

    private String getSecureHoyopass(LtuidLtoken ltuidLtoken) {
        PublicKey publicKey = keyManager.getPublicKey();
        try {
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String clientLtuidLtoken = String.format("%s:%s", ltuidLtoken.getLtuid(), ltuidLtoken.getLtoken());
            byte[] encryptedLtuidLtoken = cipher.doFinal(clientLtuidLtoken.getBytes(StandardCharsets.UTF_8));
            byte[] encodedSecureHoyopass = Base64.getEncoder().encode(encryptedLtuidLtoken);
            return new String(encodedSecureHoyopass); // secureHoyopass
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}