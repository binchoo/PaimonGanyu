package org.binchoo.paimonganyu.hoyopass.infra.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.domain.driven.SigningKeyManagerPort;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class SsmSigningKeyManager implements SigningKeyManagerPort {

    private static final String ALGORITHM = "RSA";
    private static final KeyFactory keyFactory;
    private static final Base64.Decoder base64Decoder;

    private final String publicKeyName;
    private final String privateKeyName;
    private final AWSSimpleSystemsManagement ssmClient;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    static {
        try {
            keyFactory = KeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
        base64Decoder = Base64.getDecoder();
    }

    @PostConstruct
    private void init() {
        validateFields(base64Decoder, keyFactory, publicKeyName, privateKeyName);
        acquireKeys();
        validateFields(publicKey, privateKey);
    }

    private void validateFields(Object... objects) {
        Stream<Object> fields = Stream.of(objects);
        Stream<Object> nullFields = null;
        if ((nullFields = fields.filter(Objects::isNull)).count() > 0) {
            String message = String.format("Field validation failed: %s", nullFields.collect(Collectors.toSet()));
            RuntimeException ex = new RuntimeException(message);
            log.error(message, ex);
            throw ex;
        }
    }

    private void acquireKeys() {
        List<Parameter> ssmParameters = this.acquireSsmParameters();

        for (Parameter parameter : ssmParameters) {
            String name = parameter.getName();
            byte[] base64Decoded = base64Decoder.decode(parameter.getValue());
            if (publicKeyName.equals(name)) {
                savePublicKeyWithin(new X509EncodedKeySpec(base64Decoded));
            } else if (privateKeyName.equals(name)) {
                savePrivateKeyWithin(new PKCS8EncodedKeySpec(base64Decoded));
            }
        }
    }

    private List<Parameter> acquireSsmParameters() {
        return ssmClient.getParameters(new GetParametersRequest()
                .withNames(publicKeyName, privateKeyName).withWithDecryption(true)).getParameters();
    }

    private void savePublicKeyWithin(KeySpec keySpec) {
        this.publicKey = generatePublicKey(keySpec);
    }

    private PublicKey generatePublicKey(KeySpec keySpec) {
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void savePrivateKeyWithin(KeySpec keySpec) {
        this.privateKey = generatePrivateKey(keySpec);
    }

    private PrivateKey generatePrivateKey(KeySpec keySpec) {
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    @Override
    public String getAlgorithm() {
        return ALGORITHM;
    }
}
