package org.binchoo.paimonganyu.hoyopass.api.ds;

import org.springframework.util.MultiValueMap;

public interface DsGenerator {

    /**
     * DS 및 DS와 연계되는 xrp 관련 요청 헤더 값을 만드는 편의 메서드
     * @return DS 및 xrp 관련 값을 담은 요청 헤더
     */
    MultiValueMap<String, String> generateDsHeader();

    /**
     * DS 및 DS와 연계되는 xrp 관련 요청 헤더 값을 만드는 편의 메서드
     * @param salt
     * @return DS 및 xrp 관련 값을 담은 요청 헤더
     */
    MultiValueMap<String, String> generateDsHeader(String salt);

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
