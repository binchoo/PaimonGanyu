package org.binchoo.paimonganyu.hoyopass.utils.conversion;

import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyopass.domain.Region;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserGameRoleToHoyopassUid implements Converter<UserGameRole, Uid> {

    @Override
    public Uid convert(UserGameRole source) {

        return Uid.builder()
                .uidString(source.getGameUid())
                .characterLevel(source.getLevel())
                .characterName(source.getNickname())
                .region(Region.valueOf(source.getRegion().toUpperCase()))
                .isLumine(false)
                .build();
    }
}
