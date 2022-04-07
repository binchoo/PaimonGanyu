package org.binchoo.paimonganyu.hoyopass.infra.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.domain.driven.SingingKeyManagerPort;
import org.springframework.beans.factory.annotation.Value;
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
public class SsmSigningKeysManager implements SingingKeyManagerPort {

    private static final String ALGORITHM = "RSA";
    private static final KeyFactory keyFactory;
    private static final Base64.Decoder base64Decoder;

    @Value("${aws.ssm.publickeyname}")
    private final String publicKeyName;

    @Value("${aws.ssm.privatekeyname}")
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
            byte[] base64decoded = base64Decoder.decode(parameter.getValue());
            if (publicKeyName.equals(name)) {
                savePublicKey(new X509EncodedKeySpec(base64decoded));
            } else if (privateKeyName.equals(name)) {
                savePrivateKey(new PKCS8EncodedKeySpec(base64decoded));
            }
        }
    }

    private List<Parameter> acquireSsmParameters() {
        return ssmClient.getParameters(new GetParametersRequest()
                .withNames(publicKeyName, privateKeyName).withWithDecryption(true))
                .getParameters();
    }

    private void savePublicKey(KeySpec keySpec) {
        PublicKey x509decoded = null;
        try {
            x509decoded = keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        this.publicKey = x509decoded;
    }

    private void savePrivateKey(KeySpec keySpec) {
        PrivateKey x509decoded = null;
        try {
            x509decoded = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        this.privateKey = x509decoded;
    }

    @Override
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }
}
