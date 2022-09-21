package org.binchoo.paimonganyu.system;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.binchoo.paimonganyu.chatbot.PaimonGanyu;
import org.binchoo.paimonganyu.testconfig.TestUserHoyopassTableSetup;
import org.binchoo.paimonganyu.testconfig.TestHoyopassCredentialsConfig;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.SigningKeyManagePort;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.hoyopass.exception.CryptoException;
import org.binchoo.paimonganyu.hoyopass.exception.DuplicationException;
import org.binchoo.paimonganyu.hoyopass.exception.InactiveStateException;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityZeroException;
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
    SecureHoyopassRegister securedHoyopassRegistry;

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

        securedHoyopassRegistry.registerHoyopass(botUserId, getSecuredHoyopassString(valid0));
        UserHoyopass userHoyopass = userHoyopassCrud.findByBotUserId(botUserId)
                .orElseThrow(RuntimeException::new);

        assertThat(userHoyopass.getBotUserId()).isEqualTo(botUserId);
        assertThat(userHoyopass.getSize()).isEqualTo(1);
        assertThat(userHoyopass.getHoyopasses()).map(Hoyopass::getLtuid)
                .anyMatch(ltuid-> ltuid.equals(valid0.getLtuid()));
        assertThat(userHoyopass.getHoyopasses()).map(Hoyopass::getLtoken)
                .anyMatch(ltoken-> ltoken.equals(valid0.getLtoken()));
    }

    @Test
    void givenInvalidSecureHoyopass_registerSecureHoyopass_successes() {
        assertThrows(CryptoException.class, () ->
                securedHoyopassRegistry.registerHoyopass(
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

        assertThat(userHoyopass.getSize()).isEqualTo(2);
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

        List<Hoyopass> hoyopasses = securedHoyopassRegistry.listHoyopasses(botUserId);

        assertThat(hoyopasses).hasSize(2);
    }

    @Test
    void givenUnknownBotUserId_listHoyopasses_fails() {
        String botUserId = "botuserid uninserted";
        assertThrows(QuantityZeroException.class, ()->
                securedHoyopassRegistry.listHoyopasses(botUserId));
    }

    @Test
    void listUids_successful() {
        String botUserId = "g";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, valid0, valid1);

        List<Uid> uids = securedHoyopassRegistry.listUids(botUserId);

        userHoyopass.getHoyopasses().forEach((hoyopass)-> {
            assert(uids.containsAll(hoyopass.getUids()));
        });
    }

    @Test
    void whenHoyopassDesignated_listUids_successful() {
        String botUserId = "h";
        UserHoyopass userHoyopass = registerHoyopasses(botUserId, valid0, valid1);

        List<Uid> uids = securedHoyopassRegistry.listUids(botUserId, 0);
        assertThat(uids).containsAll(userHoyopass.listUids(0));

        uids = securedHoyopassRegistry.listUids(botUserId, 1);
        assertThat(uids).containsAll(userHoyopass.listUids(1));
    }

    @Test
    void deleteHoyopass() {
        String botUserId = "i";
        this.registerHoyopasses(botUserId, valid0, valid1);

        securedHoyopassRegistry.deleteHoyopass(botUserId, 0);

        List<Hoyopass> hoyopasses = securedHoyopassRegistry.listHoyopasses(botUserId);
        assertThat(hoyopasses).hasSize(1);
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
            assertThat(userHoyopass.getSize()).isEqualTo(creds.length);
        return userHoyopass;
    }

    private UserHoyopass registerHoyopass(String botUserId, TestHoyopassCredentialsConfig.TestCredentials cred) {
        return securedHoyopassRegistry.registerHoyopass(botUserId,
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
