package org.binchoo.paimonganyu.hoyoapi.ds;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
@Getter
@Builder(access = AccessLevel.PACKAGE)
public final class BasicDsGenerator implements DsGenerator {

    private final static String DS_SALT = "6s25p5ox5y14umn1p61aqyyvbvvl3lrt";
    private final static String HEADER_DS = "DS";
    private final static String HEADER_X_RPC_LANGUAGE = "x-rpc-language";
    private final static String HEADER_X_RPC_APP_VERSION = "x-rpc-app_version";
    private final static String HEADER_X_RPC_CLIENT_TYPE = "x-rpc-client_type";

    private MessageDigest messageDigest;
    private String xRpcLang;
    private String xRpcAppVersion;
    private String xRpcClientType;

    @Override
    public MultiValueMap<String, String> generateDsHeader() {
        return this.generateDsHeader(DS_SALT);
    }

    @Override
    public MultiValueMap<String, String> generateDsHeader(String salt) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        headers.add(HEADER_X_RPC_LANGUAGE, this.xRpcLang);
        headers.add(HEADER_X_RPC_APP_VERSION, this.xRpcAppVersion);
        headers.add(HEADER_X_RPC_CLIENT_TYPE, this.xRpcClientType);
        headers.add(HEADER_DS, this.generateDs(salt));

        return headers;
    }

    @Override
    public String generateDs() {
        return this.generateDs(DS_SALT);
    }

    @Override
    public String generateDs(String salt) {
        final long t = T();
        final String r = R();
        final String h = H(salt, t, r);

        return String.format("%s,%s,%s", t, r, h);
    }

    private long T() {
        return Instant.now().getEpochSecond();
    }

    private String R() {
        return new Random().ints(6, 'a', 'z')
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String H(String salt, long t, String r) {
        byte[] digest = messageDigest.digest(
                String.format("salt=%s&t=%s&r=%s", salt, t, r).getBytes());

        return DatatypeConverter.printHexBinary(digest).toLowerCase(); // must be a lowercase string
    }

    public static BasicDsGenerator create() {
        try {
            return BasicDsGenerator.builder()
                    .messageDigest(MessageDigest.getInstance(MessageDigestAlgorithms.MD5))
                    .xRpcLang("ko-kr").xRpcAppVersion("1.5.0").xRpcClientType("5")
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a MessageDigest for BasicDsGenerator");
        }
    }
}
