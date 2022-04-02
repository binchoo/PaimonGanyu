package org.binchoo.paimonganyu.hoyoapi.ds;

public interface DsGenerator {

    /**
     * 클래스의 DS_SALT를 솔트로하여 DS를 생산합니다.
     * @return DS 값
     */
    String generateDs();

    /**
     * 주어진 솔트로 DS를 생산합니다.
     * @param salt
     * @return DS 값
     */
    String generateDs(String salt);
}
