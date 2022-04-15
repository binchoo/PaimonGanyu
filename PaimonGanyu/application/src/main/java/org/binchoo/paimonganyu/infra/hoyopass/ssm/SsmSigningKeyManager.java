package org.binchoo.paimonganyu.infra.hoyopass.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.driven.SigningKeyManagerPort;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Lazy
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
            throw new IllegalStateException(e);
        }
        base64Decoder = Base64.getDecoder();
    }

    /**
     * {@link SigningKeyManagerPort} 구현체를 생성합니다.
     * 이 구현체는 AWS SSM 파라미터 저장소에 암호화용 공개키, 사설키를 가져오는 역할로 의도되었습니다.
     * AWS SSM로의 접근 권한은 머신의 IAM Role 혹은 {@link com.amazonaws.auth.AWSCredentialsProvider}에 의해 보호되어야 합니다.
     * @param publicKeyName SSM 파라미터 저장소에 저장한 공개키의 이름
     * @param privateKeyName SSM 파라미터 저장소에 저장한 사설키의 이름
     * @param ssmClient AWS SSM 클라이언트
     * @throws IllegalStateException SSM 파라미터에 저장된 값이 Base64 인코딩 형식이 아닐 때,
     * SSM 파라미터 값 분석 결과 RSA 공개키/사설키 형식이 아닐 때, publickKey나 privateKey 필드 작성에 실패했을 때.
     */
    public SsmSigningKeyManager(String publicKeyName, String privateKeyName, AWSSimpleSystemsManagement ssmClient) {
        this.publicKeyName = publicKeyName;
        this.privateKeyName = privateKeyName;
        this.ssmClient = ssmClient;
        init();
    }

    private void init() {
        validateFields(base64Decoder, keyFactory, publicKeyName, privateKeyName);
        acquireKeys();
        validateFields(publicKey, privateKey);
    }

    private void validateFields(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                String message = "Field validation has failed.";
                IllegalStateException ex = new IllegalStateException(message);
                log.error(message, ex);
                throw ex;
            }
        }
    }

    private void acquireKeys() {
        List<Parameter> ssmParameters = this.acquireSsmParameters();
        for (Parameter parameter : ssmParameters) {
            String name = parameter.getName();
            try {
                byte[] base64Decoded = base64Decoder.decode(parameter.getValue());
                if (publicKeyName.equals(name)) {
                    savePublicKeyWithin(base64Decoded);
                } else if (privateKeyName.equals(name)) {
                    savePrivateKeyWithin(base64Decoded);
                }
            } catch (IllegalArgumentException e) {
                String message = "파라미터에 저장된 값이 Base64 인코딩되지 않은 것 같습니다.";
                log.error(message, e);
                throw new IllegalStateException(message, e);
            }
        }
    }

    private List<Parameter> acquireSsmParameters() {
        return ssmClient.getParameters(new GetParametersRequest()
                .withNames(publicKeyName, privateKeyName).withWithDecryption(true)).getParameters();
    }

    private void savePublicKeyWithin(byte[] x509Encoded) {
        this.publicKey = generatePublicKey(new X509EncodedKeySpec(x509Encoded));
    }

    private PublicKey generatePublicKey(KeySpec keySpec) {
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            log.warn("Could not import a public key", e);
        }
        return null;
    }

    private void savePrivateKeyWithin(byte[] pkcs8Encoded) {
        this.privateKey = generatePrivateKey(new PKCS8EncodedKeySpec(pkcs8Encoded));
    }

    private PrivateKey generatePrivateKey(KeySpec keySpec) {
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            log.warn("Could not import a private key", e);
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
