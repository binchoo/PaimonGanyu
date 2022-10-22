package org.binchoo.paimonganyu.system;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.binchoo.paimonganyu.chatbot.PaimonGanyu;
import org.binchoo.paimonganyu.hoyopass.exception.*;
import org.binchoo.paimonganyu.testconfig.TestUserHoyopassTableSetup;
import org.binchoo.paimonganyu.testconfig.TestHoyopassCredentialsConfig;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.SigningKeyManagePort;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.item.UserHoyopassItem;
import org.binchoo.paimonganyu.service.hoyopass.SecureHoyopassRegister;
import org.binchoo.paimonganyu.testconfig.TestAmazonClientsConfig;
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
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {
        TestAmazonClientsConfig.class, TestHoyopassCredentialsConfig.class, TestUserHoyopassTableSetup.class,
        PaimonGanyu.class})
class SecuredHoyopassRegistryLocalSystemTest {

    @Autowired
    SecureHoyopassRegister hoyopassRegister;

    @Autowired
    SigningKeyManagePort signingKeyManager;

    @Autowired
    UserHoyopassCrudPort userHoyopassCrud;

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @Autowired
    @Qualifier("valid0")
    TestHoyopassCredentialsConfig.TestCredentials valid0;

    @Autowired
    @Qualifier("valid1")
    TestHoyopassCredentialsConfig.TestCredentials valid1;

    @Autowired
    TestHoyopassCredentialsConfig.InvalidTestCredentials invalid0;

    @BeforeEach
    void cleanEntries() {
        new UserHoyopassTableBuilder(amazonDynamoDB).createTable();
        userHoyopassCrud.deleteAll();
    }

    @AfterEach
    void printEveryEntry() {
        System.out.println(userHoyopassCrud.findAll());
    }

    @Test
    void givenSecuredHoyopass_registerSecureHoyopass_successes() {
        String botUserId = "foobar";

        hoyopassRegister.registerHoyopass(botUserId, getSecuredHoyopassString(valid0));
        UserHoyopass userHoyopass = userHoyopassCrud.findByBotUserId(botUserId)
                .orElseThrow(RuntimeException::new);

        assertThat(userHoyopass.getBotUserId()).isEqualTo(botUserId);
        assertThat(userHoyopass.size()).isEqualTo(1);
        assertThat(userHoyopass.listHoyopasses()).map(Hoyopass::getLtuid)
                .anyMatch(ltuid-> ltuid.equals(valid0.getLtuid()));
        assertThat(userHoyopass.listHoyopasses()).map(Hoyopass::getLtoken)
                .anyMatch(ltoken-> ltoken.equals(valid0.getLtoken()));
    }

    @Test
    void givenInvalidSecureHoyopass_registerSecureHoyopass_successes() {
        assertThrows(CryptoException.class, () ->
                hoyopassRegister.registerHoyopass(
                        "foobar", "fakeSecureHoyopassString"));
    }

    @Test
    void givenOneHoyopass_registerHoyopass_successful() {
        UserHoyopass userHoyopass = registerHoyopass("a", valid0);
        assertThat(userHoyopass.getBotUserId()).isEqualTo("a");
    }

    @Test
    void givenFakeHoyopass_registerHoyopass_fails() {
        assertThrows(InactiveStateException.class, ()->
                registerHoyopass("b", invalid0));
    }

    @Test
    void givenTwoHoyopasses_registerHoyopass_successful() {
        String botUserId = "c";

        UserHoyopass userHoyopass = registerHoyopasses(botUserId, valid0, valid1);

        assertThat(userHoyopass.size()).isEqualTo(2);
    }

    @Test
    void givenDuplicateHoyopasses_registeHoyopass_fails() {
        registerHoyopass("d", valid0);
        assertThrows(DuplicationException.class, ()-> {
            registerHoyopass("d", valid0);
        });
    }

    @Test
    void givenHoyopassesForManyUsers_registeHoyopass_successful() {
        IntStream.of(1, 10).forEach(i-> {
            String botUserId = "botUser" + i;
            UserHoyopass userHoyopass = registerHoyopass(botUserId, valid1);
            assertThat(userHoyopass.getBotUserId()).isEqualTo(botUserId);
        });
    }

    @Test
    void listHoyopasses_successful() {
        String botUserId = "e";
        this.registerHoyopasses(botUserId, valid0, valid1);

        List<Hoyopass> hoyopasses = hoyopassRegister.findUserHoyopass(botUserId).listHoyopasses();

        assertThat(hoyopasses).hasSize(2);
    }

    @Test
    void givenUnknownBotUserId_listHoyopasses_fails() {
        String botUserId = "botuserid uninserted";
        assertThrows(NoHoyopassException.class, ()->
                hoyopassRegister.findUserHoyopass(botUserId));
    }

    @Test
    void listUids_successful() {
        String botUserId = "g";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, valid0, valid1);

        List<Uid> uids = hoyopassRegister.listUids(botUserId);

        userHoyopass.listHoyopasses().forEach((hoyopass)-> {
            assert(uids.containsAll(hoyopass.getUids()));
        });
    }

    @Test
    void whenHoyopassDesignated_listUids_successful() {
        String botUserId = "h";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, valid0, valid1);

        List<Uid> uids = hoyopassRegister.listUids(botUserId, 0);
        assertThat(uids).containsAll(userHoyopass.listUidsAt(0));

        uids = hoyopassRegister.listUids(botUserId, 1);
        assertThat(uids).containsAll(userHoyopass.listUidsAt(1));
    }

    @Test
    void deleteHoyopass() {
        String botUserId = "i";
        this.registerHoyopasses(botUserId, valid0, valid1);

        hoyopassRegister.deleteHoyopass(botUserId, 0);

        List<Hoyopass> hoyopasses = hoyopassRegister.findUserHoyopass(botUserId).listHoyopasses();
        assertThat(hoyopasses).hasSize(1);
    }

    @Test
    void givenHoyopassWithMultipleUids_deleteUid_successes() {
        String botUserId = "with 2 uids";
        this.registerHoyopasses(botUserId, valid0);

        UserHoyopass user = hoyopassRegister.findUserHoyopass(botUserId);
        Hoyopass pass = user.getHoyopassAt(0);
        Uid uid = pass.getUids().get(0);

        int beforeSize = user.listUids().size();
        hoyopassRegister.deleteUid(botUserId, uid.getUidString());

        UserHoyopass savedUser = hoyopassRegister.findUserHoyopass(botUserId);
        int afterSize = savedUser.listUids().size();
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    @Test
    void givenHoyopassWithSingleUid_deleteUid_successes() {
        String botUserId = "with single uid";
        this.registerHoyopasses(botUserId, valid1);

        UserHoyopass user = hoyopassRegister.findUserHoyopass(botUserId);
        Hoyopass pass = user.getHoyopassAt(0);
        Uid uid = pass.getUids().get(0);

        int beforeSize = user.listUids().size();
        assertThrows(ImmortalUidException.class,
                ()-> hoyopassRegister.deleteUid(botUserId, uid.getUidString()));

        UserHoyopass savedUser = hoyopassRegister.findUserHoyopass(botUserId);
        int afterSize = savedUser.listUids().size();
        assertThat(afterSize).isEqualTo(beforeSize);
    }

    private String getSecuredHoyopassString(TestHoyopassCredentialsConfig.TestCredentials cred) {
        PublicKey publicKey = signingKeyManager.getPublicKey();
        try {
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String clientLtuidLtoken = String.format("%s:%s:%s", cred.getLtuid(),
                    cred.getLtoken(), cred.getCookieToken());
            byte[] encryptedLtuidLtoken = cipher.doFinal(clientLtuidLtoken.getBytes(StandardCharsets.UTF_8));
            byte[] encodedSecureHoyopass = Base64.getEncoder().encode(encryptedLtuidLtoken);
            return new String(encodedSecureHoyopass); // secureHoyopass
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private UserHoyopass registerHoyopasses(String botUserId,
                                            TestHoyopassCredentialsConfig.TestCredentials... creds) {

        UserHoyopass userHoyopass = null;
        for (var cred : creds)
            userHoyopass = this.registerHoyopass(botUserId, cred);
        if (userHoyopass != null)
            assertThat(userHoyopass.size()).isEqualTo(creds.length);
        return userHoyopass;
    }

    private UserHoyopass registerHoyopass(String botUserId, TestHoyopassCredentialsConfig.TestCredentials cred) {
        return hoyopassRegister.registerHoyopass(botUserId,
                HoyopassCredentials.builder()
                        .ltuid(cred.getLtuid())
                        .ltoken(cred.getLtoken())
                        .cookieToken(cred.getCookieToken())
                        .build());
    }

    private static final class UserHoyopassTableBuilder {

        private final AmazonDynamoDB dynamoClient;

        public UserHoyopassTableBuilder(AmazonDynamoDB dynamoClient) {
            this.dynamoClient = dynamoClient;
        }

        public void createTable() {
            DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
            CreateTableRequest request = mapper.generateCreateTableRequest(UserHoyopassItem.class);
            request.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
            try {
                dynamoClient.createTable(request);
            } catch (Exception e) { // It's ok. Do nothing.
                e.printStackTrace();
            }
            dynamoClient.listTables().getTableNames().forEach(System.out::println);
        }
    }
}
