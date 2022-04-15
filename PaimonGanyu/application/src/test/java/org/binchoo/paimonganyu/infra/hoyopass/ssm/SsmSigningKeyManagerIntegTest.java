package org.binchoo.paimonganyu.infra.hoyopass.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import org.binchoo.paimonganyu.hoyopass.driven.SigningKeyManagerPort;
import org.binchoo.paimonganyu.testamazonclients.TestAmazonClientsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author : jbinchoo
 * @since : 2022-04-15
 */
@Configuration
@TestPropertySource("classpath:amazon.properties")
@SpringJUnitConfig({TestAmazonClientsConfig.class, SsmSigningKeyManagerIntegTest.class})
class SsmSigningKeyManagerIntegTest {

    @Lazy
    @Bean
    SigningKeyManagerPort ssmSigningKeyManager(
            @Value("${amazon.ssm.hoyopass.publickeyname}") String publicKeyName,
            @Value("${amazon.ssm.hoyopass.privatekeyname}") String privateKeyName, AWSSimpleSystemsManagement ssmClient) {
        return new SsmSigningKeyManager(publicKeyName, privateKeyName, ssmClient);
    }

    @Autowired
    SigningKeyManagerPort ssmSigningKeyManager;

    @Test
    void getPublicKey() {
        var privateKey = ssmSigningKeyManager.getPrivateKey();
        assertThat(privateKey.getFormat()).isEqualTo("PKCS#8");
    }

    @Test
    void getPrivateKey() {
        var publicKey = ssmSigningKeyManager.getPublicKey();
        assertThat(publicKey.getFormat()).isEqualTo("X.509");
    }

    @Test
    void getAlgorithm() {
        var algorithm = ssmSigningKeyManager.getAlgorithm();
        var privateKey = ssmSigningKeyManager.getPrivateKey();
        var publicKey = ssmSigningKeyManager.getPublicKey();

        assertThat(privateKey.getAlgorithm()).isEqualTo(algorithm);
        assertThat(publicKey.getAlgorithm()).isEqualTo(algorithm);
    }
}