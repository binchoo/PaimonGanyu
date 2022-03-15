package org.binchoo.paimonganyu.hoyopass.infra.web;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatars;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRoles;
import org.binchoo.paimonganyu.hoyoapi.response.HoyoResponse;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Region;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.outport.HoyopassSearch;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class HoyopassHoyolabApiSearch implements HoyopassSearch {

    private final HoyolabAccountApi accountApi;
    private final HoyolabGameRecordApi gameRecordApi;
    private final ConversionService converters;

    @Override
    public Hoyopass fillUids(Hoyopass hoyopass) {
        LtuidLtoken ltuidLtoken = getLtuidLtoken(hoyopass);

        HoyoResponse<UserGameRoles> apiResponse = accountApi.getUserGameRoles(ltuidLtoken);
        List<UserGameRole> userGameRoles = apiResponse.getData().getList();

        List<Uid> newUids = userGameRoles.stream()
                .map(ugr-> Uid.builder()
                        .uidString(ugr.getGameUid())
                        .characterLevel(ugr.getLevel())
                        .characterName(ugr.getNickname())
                        .region(Region.fromString(ugr.getRegion()))
                        .isLumine(resolveIsLumine(ltuidLtoken, ugr.getGameUid(), ugr.getRegion()))
                        .build())
                .collect(Collectors.toList());

        return hoyopass.toBuilder().uids(newUids).build();
    }

    private LtuidLtoken getLtuidLtoken(Hoyopass hoyopass) {
        return converters.convert(hoyopass, LtuidLtoken.class);
    }

    /**
     * 해당 uid가 루미네를 갖고 있는지 미호요 API를 호출하여 판단합니다.
     * @param ltuidLtoken
     * @param uid
     * @param region
     * @return
     */
    private boolean resolveIsLumine(LtuidLtoken ltuidLtoken, String uid, String region) {
        HoyoResponse<GenshinAvatars> apiResponse = gameRecordApi.getAllCharacter(ltuidLtoken, uid, region);
        return apiResponse.getData().containsLumine();
    }
}
