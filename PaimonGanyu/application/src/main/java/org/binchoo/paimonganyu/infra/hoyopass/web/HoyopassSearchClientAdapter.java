package org.binchoo.paimonganyu.infra.hoyopass.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError;
import org.binchoo.paimonganyu.hoyoapi.pojo.*;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.driven.HoyopassSearchClientPort;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class HoyopassSearchClientAdapter implements HoyopassSearchClientPort {

    private final HoyolabAccountApi accountApi;
    private final HoyolabGameRecordApi gameRecordApi;

    @Override
    public List<Uid> findUids(Hoyopass hoyopass) {
        LtuidLtoken ltuidLtoken = getLtuidLtoken(hoyopass);
        HoyoResponse<UserGameRoles> apiResponse = accountApi.getUserGameRoles(ltuidLtoken);
        List<UserGameRole> userGameRoles = apiResponse.getData().getList();
        return mapToUid(userGameRoles, ltuidLtoken);
    }

    private List<Uid> mapToUid(List<UserGameRole> userGameRoles, LtuidLtoken ltuidLtoken) {
        return userGameRoles.stream()
                .map(ugr -> Uid.builder()
                        .uidString(ugr.getGameUid())
                        .characterLevel(ugr.getLevel())
                        .characterName(ugr.getNickname())
                        .region(Region.fromString(ugr.getRegion()))
                        .isLumine(containsLumine(ltuidLtoken, ugr))
                        .build())
                .collect(Collectors.toList());
    }

    private LtuidLtoken getLtuidLtoken(Hoyopass hoyopass) {
        return new LtuidLtoken(hoyopass.getLtuid(), hoyopass.getLtoken());
    }

    /**
     * 해당 UserGameRole이 루미네를 갖고 있는지 미호요 API를 호출하여 판단합니다.
     * @param ltuidLtoken 미호요 통행증 쿠키
     * @param userGameRole
     * @return 이 UID가 루미네를 캐릭터로 포함하고 있는지 여부
     */
    private boolean containsLumine(LtuidLtoken ltuidLtoken, UserGameRole userGameRole) {
        try {
            String uid = userGameRole.getGameUid();
            String region = userGameRole.getRegion();
            HoyoResponse<GenshinAvatars> apiResponse = gameRecordApi.getAllAvartar(ltuidLtoken, uid, region);
            return apiResponse.getData().containsLumine();
        } catch (NotLoggedInError e) {
            log.warn("This account cannot use HoYoLab api. Please create your nickname at HoYoLab main homepage.", e);
            return false;
        }
    }
}
