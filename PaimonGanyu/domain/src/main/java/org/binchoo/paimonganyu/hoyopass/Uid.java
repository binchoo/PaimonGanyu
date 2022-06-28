package org.binchoo.paimonganyu.hoyopass;

import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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
