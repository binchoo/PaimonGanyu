package org.binchoo.paimonganyu.hoyopass.domain;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Objects;

@Slf4j
public class SecureHoyopass {

    private static final String DELIMETER = ":";
    private static final Base64.Decoder base64Decoder = Base64.getDecoder();

    private final String secureHoyopassString;

    private String ltuid;
    private String ltoken;

    public SecureHoyopass(String secureHoyopassString) {
        this.secureHoyopassString = secureHoyopassString;
    }

    public void decrypt(PrivateKey privateKey) {
        try {
            byte[] hoyopassComposite = decryptWithin(
                    base64Decoder.decode(secureHoyopassString), privateKey);
            saveToFields(hoyopassComposite);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                    | BadPaddingException | IllegalBlockSizeException e) {
            log.error("Could not process a decryption for Hoyopass", e);
            throw new IllegalStateException(e);
        }
    }

    private byte[] decryptWithin(byte[] base64Decoded, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(base64Decoded);
    }

    private void saveToFields(byte[] hoyopassComposite) {
        String[] split = new String(hoyopassComposite).split(DELIMETER);
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
