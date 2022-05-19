package org.binchoo.paimonganyu.hoyopass;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

@Slf4j
public class SecureHoyopassCredentials {

    private static final String DELIMETER = ":";
    private static final Base64.Decoder base64Decoder = Base64.getDecoder();

    private final String signedString;

    private HoyopassCredentials credentials;

    public SecureHoyopassCredentials(String signedString) {
        this.signedString = signedString;
    }

    public void decrypt(PrivateKey privateKey) {
        try {
            byte[] hoyopassComposite = decryptWithin(
                    base64Decoder.decode(signedString), privateKey);
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
        assert split.length == 3;
        String ltuid = split[0];
        String ltoken = split[1];
        String cookieToken = split[2];
        this.credentials = HoyopassCredentials.builder()
                .ltuid(ltuid).ltoken(ltoken).cookieToken(cookieToken).build();
    }

    public HoyopassCredentials getCredentials() {
        return this.credentials;
    }
}
