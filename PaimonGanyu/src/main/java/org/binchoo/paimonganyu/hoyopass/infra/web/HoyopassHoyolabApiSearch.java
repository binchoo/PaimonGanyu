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

@RequiredArgsConstructor
@Component
public class HoyopassHoyolabApiSearch implements HoyopassSearch {

    private final HoyolabAccountApi accountApi;
    private final HoyolabGameRecordApi gameRecordApi;
    private final ConversionService converters;

    @Override
    public Hoyopass fillUids(Hoyopass hoyopass) {
        LtuidLtoken ltuidLtoken = getLtuidLtoken(hoyopass);
        HoyoResponse<UserGameRoles> apiResponse =
                accountApi.getUserGameRoles(ltuidLtoken);

        List<Uid> newUids = new LinkedList<>();
        List<UserGameRole> userGameRoles = apiResponse.getData().getList();
        userGameRoles.forEach(ugr-> {
            Uid newUid = converters.convert(ugr, Uid.class);
            fillIsLumine(newUid, ltuidLtoken);
        });

        return hoyopass.toBuilder().uids(newUids).build();
    }

    private LtuidLtoken getLtuidLtoken(Hoyopass hoyopass) {
        return converters.convert(hoyopass, LtuidLtoken.class);
    }

    /**
     * 주어진 Uid가 루미네를 갖고 있는지 확인하여 isLumine 값을 설정합니다.
     * @param uid
     * @param ltuidLtoken
     * @return 루미네 보유 여부를 표시하고 있는 {@link Uid} 객체
     */
    private Uid fillIsLumine(Uid uid, LtuidLtoken ltuidLtoken) {
        HoyoResponse<GenshinAvatars> apiResponse =
                gameRecordApi.getAllCharacter(ltuidLtoken, uid.getUidString(), uid.getRegion().lowercase());

        return uid.toBuilder()
                .isLumine(apiResponse.getData().containsLumine())
                .build();
    }
}
