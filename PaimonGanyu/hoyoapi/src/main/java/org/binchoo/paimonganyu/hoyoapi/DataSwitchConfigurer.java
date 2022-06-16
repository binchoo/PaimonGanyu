package org.binchoo.paimonganyu.hoyoapi;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;

/**
 * @author jbinchoo
 * @since 2022/06/16
 */
public interface DataSwitchConfigurer {

    /**
     * 유저의 데이터 스위치를 전부 켠다.
     * @param ltuidLtoken 유저의 쿠키 토큰
     * @return 수행 성공 여부
     */
    boolean turnOnSwitches(LtuidLtoken ltuidLtoken);

    /**
     * 유저의 데이터 스위치를 전부 끈다.
     * @param ltuidLtoken 유저의 쿠키 토큰
     * @return 수행 성공 여부
     */
    boolean turnOffSwitches(LtuidLtoken ltuidLtoken);
}
