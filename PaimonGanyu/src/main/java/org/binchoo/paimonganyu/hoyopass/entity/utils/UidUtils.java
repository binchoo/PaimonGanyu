package org.binchoo.paimonganyu.hoyopass.entity.utils;

import org.binchoo.paimonganyu.hoyopass.api.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyopass.entity.Uid;

public class UidUtils {

    public static Uid fromUserGameRole(UserGameRole userGameRole) {
        return Uid.builder()
                .isLumine(false)
                .characterLevel(userGameRole.getLevel())
                .characterName(userGameRole.getNickname())
                .region(Uid.Region.valueOf(userGameRole.getRegionName().toUpperCase()))
                .uid(userGameRole.getGameUid())
                .build();
    }
}
