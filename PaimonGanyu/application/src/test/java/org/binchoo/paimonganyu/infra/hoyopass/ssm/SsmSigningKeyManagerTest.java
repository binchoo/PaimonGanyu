package org.binchoo.paimonganyu.infra.hoyopass.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author : jbinchoo
 * @since : 2022-04-15
 */
class SsmSigningKeyManagerTest {

    String publicKeyName = "foo";
    String privateKeyName = "bar";

    AWSSimpleSystemsManagement ssmClient = Mockito.mock(AWSSimpleSystemsManagement.class);
    GetParametersResult mockParametersResult = Mockito.mock(GetParametersResult.class);
    Parameter paramAsPublicKey = Mockito.mock(Parameter.class);
    Parameter paramAsPrivateKey = Mockito.mock(Parameter.class);

    Base64.Encoder base64Encoder = Base64.getEncoder();

    @BeforeEach
    public void commonWiring() {
        when(ssmClient.getParameters(any())).thenReturn(mockParametersResult);
        when(mockParametersResult.getParameters())
                .thenReturn(Arrays.asList(paramAsPublicKey, paramAsPublicKey));
        when(paramAsPublicKey.getName()).thenReturn(publicKeyName);
        when(paramAsPrivateKey.getName()).thenReturn(privateKeyName);
    }

    @Test
    void givenNonBase64ParameterValues_initialization_fails() {
        givenNonBase64ParameterValues();
        assertThrows(IllegalStateException.class, ()-> new SsmSigningKeyManager(
                "foo", "bar", ssmClient));
    }

    private void givenNonBase64ParameterValues() {
        when(paramAsPublicKey.getValue()).thenReturn("non-base64 string");
        when(paramAsPrivateKey.getValue()).thenReturn("non-base64 string");
    }

    @Test
    void givenNonCryptographicParameterValues_initialization_fails() {
        givenNonCryptographicParameterValues();
        assertThrows(IllegalStateException.class, ()-> new SsmSigningKeyManager(
                "foo", "bar", ssmClient));
    }

    private void givenNonCryptographicParameterValues() {
        when(paramAsPublicKey.getValue())
                .thenReturn(base64Encoder.encodeToString("this is non-cryptographic bytes".getBytes(StandardCharsets.UTF_8)));
        when(paramAsPrivateKey.getValue())
                .thenReturn(base64Encoder.encodeToString("this is non-cryptographic bytes".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void givenNonCryptographicParameterValueForPrivateKey_initialization_fails() {
        givenNonCryptographicParameterValueForPrivateKey();
        assertThrows(IllegalStateException.class, ()-> new SsmSigningKeyManager(
                "foo", "bar", ssmClient));
    }

    private void givenNonCryptographicParameterValueForPrivateKey() {
        when(paramAsPublicKey.getValue())
                .thenReturn(base64Encoder.encodeToString(generatePublicKey().getEncoded()));
        when(paramAsPrivateKey.getValue())
                .thenReturn(base64Encoder.encodeToString("this is non-cryptographic bytes".getBytes(StandardCharsets.UTF_8)));
    }

    private PublicKey generatePublicKey() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair().getPublic();
    }
}