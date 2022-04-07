package org.binchoo.paimonganyu.hoyopass.domain;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class SecureHoyopass {

    private static final String signingAlgorithm = "RSA";

    public SecureHoyopass(String secureHoyopassString, PrivateKey privateKey) {

        try {
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public void decrypt() {
    }

    public String getLtuid() {
        return null;
    }

    public String getLtoken() {
        return null;
    }
}
