package org.binchoo.paimonganyu.hoyopass.domain;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.domain.driven.SigningKeyManagerPort;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Objects;

@Slf4j
public class SecureHoyopass {

    private static final String DELIMETER = ":";

    private final String secureHoyopassString;
    private String ltuid;
    private String ltoken;

    public SecureHoyopass(String secureHoyopassString) {
        this.secureHoyopassString = secureHoyopassString;
    }

    public void decrypt(PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedHoyopass = cipher.doFinal(secureHoyopassString.getBytes());
            save(decryptedHoyopass);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                    | BadPaddingException | IllegalBlockSizeException e) {
            log.error("Could not create a Cipher.", e);
            throw new RuntimeException(e);
        }
    }

    private void save(byte[] decryptedHoyopass) {
        String[] split = new String(decryptedHoyopass).split(DELIMETER);
        assert split.length == 2;
        this.ltuid = split[0];
        this.ltoken = split[1];
    }

    public String getLtuid() {
        Objects.requireNonNull(ltuid);
        return ltuid;
    }

    public String getLtoken() {
        Objects.requireNonNull(ltoken);
        return ltoken;
    }
}
