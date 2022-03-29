package org.binchoo.paimonganyu.hoyopass.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Uid {

    /**
     * 원신 계정의 UID
     */
    private String uidString;

    /**
     * 원신 계정의 레벨
     */
    private Integer characterLevel;

    /**
     * 원신 계정의 이름
     */
    private String characterName;

    /**
     * 원신 계정의 서버
     */
    private Region region;

    /**
     * 여행자? 남행자?
     */
    private Boolean isLumine;
}
