package org.binchoo.paimonganyu.hoyoapi.tool;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.binchoo.paimonganyu.hoyoapi.tool.DsGenerator;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Random;

/**
 *
 * <p> 미호요가 구현한 DS 생성 알고리즘을 그대로 구현합니다. DS := "t,r,h"
 * <p> 여기서, t := 1970-01-01 부터 현 시점까지의 지나간 초
 * <p> r := 임의의 아스키 6문자
 * <p> h := "salt=%s&t=%s&r=%s".format(salt, t, r)를 MD5로 다이제스트 한 뒤 16진수로 표현한 문자열
 */
public class DefaultDsGenerator implements DsGenerator {

    private static final String DS_SALT = "6s25p5ox5y14umn1p61aqyyvbvvl3lrt";

    private final Random random;

    private MessageDigest messageDigest;

    public DefaultDsGenerator() {
        try {
            messageDigest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not find MD5 MessageDigestAlgorithm class.");
        }
        this.random = new Random();
    }

    @Override
    public String generateDs() {
        return this.generateDs(DS_SALT);
    }

    @Override
    public String generateDs(String salt) {
        final long t = timestamp();
        final String r = asciiRandoms(6);
        final String h = hexDigest(salt, t, r);
        return String.format("%s,%s,%s", t, r, h);
    }

    private long timestamp() {
        return Instant.now().getEpochSecond();
    }

    private String asciiRandoms(int length) {
        return random.ints(length, 'a', 'z')
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    private String hexDigest(String salt, long t, String r) {
        byte[] digest = messageDigest.digest(
                String.format("salt=%s&t=%s&r=%s", salt, t, r).getBytes());
        return DatatypeConverter.printHexBinary(digest).toLowerCase(); // must be a lowercase string
    }
}
