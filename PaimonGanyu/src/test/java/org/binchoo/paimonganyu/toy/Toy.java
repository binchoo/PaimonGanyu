package org.binchoo.paimonganyu.toy;


import org.springframework.data.util.Pair;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * packageName : org.binchoo.paimonganyu.toy
 * fileName : Toy
 * author : jbinchoo
 * date : 2022-04-07
 * description :
 */
public class Toy {

    final static PublicKey publicKey;
    final static PrivateKey privateKey;

    static {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);

            KeyPair keyPair = generator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            System.out.println("------------------------");
            System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            System.out.println("------------------------");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("what the heck", e);
        }
    }

    public static void main(String[] args) {
        String ltuid = "12345";
        String ltoken = "abced";

        final String secureHoyopass = clientSideWorkflow(ltuid, ltoken);
        final Pair<String, String> hoyopass = serverSideWorkflow(secureHoyopass);

        assert(ltuid.equals(hoyopass.getFirst()));
        assert(ltoken.equals(hoyopass.getSecond()));
    }

    private static String clientSideWorkflow(String ltuid, String ltoken) {
        // ltuid:ltoken
        String ltuidLtoken = String.format("%s:%s", ltuid, ltoken);
        // RSA 공캐키 암호화
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(ltuidLtoken.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getEncoder().encodeToString(encrypted);
            System.out.println(encoded);
            return encoded;
        } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Pair<String, String> serverSideWorkflow(String secureHoyopass) {
        // BASE64 디코딩
        byte[] encrypted = Base64.getDecoder().decode(secureHoyopass.getBytes());
        // RAS 사설키 복호화
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] unencrypted = cipher.doFinal(encrypted);
            String hoyopass = new String(unencrypted);
            String[] pieces = hoyopass.split(":");
            Pair<String, String> userHoyopass = Pair.of(pieces[0], pieces[1]);
            System.out.println(userHoyopass);
            return userHoyopass;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
