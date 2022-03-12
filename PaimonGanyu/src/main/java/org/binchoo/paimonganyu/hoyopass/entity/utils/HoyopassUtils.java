package org.binchoo.paimonganyu.hoyopass.entity.utils;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;

public class HoyopassUtils {

    public static LtuidLtoken ltuidLtoken(Hoyopass hoyopass) {
        return new LtuidLtoken(hoyopass.getLtuid(), hoyopass.getLtoken());
    }
}
