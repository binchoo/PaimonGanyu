package org.binchoo.paimonganyu.hoyopass.infra.web;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyoapi.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatars;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRoles;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.outport.HoyopassSearch;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class HoyopassHoyolabApiSearch implements HoyopassSearch {

    private final HoyolabAccountApi accountApi;
    private final HoyolabGameRecordApi gameRecordApi;
    private final ConversionService converters;

    @Override
    public Hoyopass fillUids(Hoyopass hoyopass) {
        HoyoResponse<UserGameRoles> apiResponse =
                accountApi.getUserGameRoles(getLtuidLtoken(hoyopass));

        List<UserGameRole> userGameRoles = apiResponse.getData().getList();
        List<Uid> newUids = new LinkedList<>();
        userGameRoles.forEach(ugr->
            newUids.add(converters.convert(ugr, Uid.class))
        );

        return hoyopass.toBuilder().uids(newUids).build();
    }

    @Override
    public Hoyopass fillIsLumines(Hoyopass hoyopass) {
        LtuidLtoken ltuidLtoken = getLtuidLtoken(hoyopass);
        List<Uid> uids = hoyopass.getUids();

        if (Objects.isNull(uids))
            throw new IllegalStateException("UID 리스트가 null이어서 isLumine 값을 채울 수 없습니다.");

        List<Uid> newUids = new LinkedList<>();
        uids.forEach(uid->
                newUids.add(fillIsLumine(uid, ltuidLtoken))
        );

        return hoyopass.toBuilder().uids(newUids).build();
    }

    private LtuidLtoken getLtuidLtoken(Hoyopass hoyopass) {
        return converters.convert(hoyopass, LtuidLtoken.class);
    }

    public Uid fillIsLumine(Uid uid, LtuidLtoken ltuidLtoken) {
        HoyoResponse<GenshinAvatars> apiResponse =
                gameRecordApi.getAllCharacter(ltuidLtoken, uid.getUidString(), uid.getRegion().lowercase());

        return uid.toBuilder()
                .isLumine(apiResponse.getData().containsLumine())
                .build();
    }
}
