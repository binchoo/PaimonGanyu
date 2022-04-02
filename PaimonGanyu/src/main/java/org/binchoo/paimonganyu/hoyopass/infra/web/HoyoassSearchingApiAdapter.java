package org.binchoo.paimonganyu.hoyopass.infra.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.apis.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.apis.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatars;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRoles;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Region;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.driven.HoyopassSearchPort;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class HoyoassSearchingApiAdapter implements HoyopassSearchPort {

    private final HoyolabAccountApi accountApi;
    private final HoyolabGameRecordApi gameRecordApi;
    private final ConversionService converters;

    /**
     * @throws NotLoggedInError 호요랩에서 닉네임 설정을 하지 않았을 때.
     */
    @Override
    public List<Uid> findUids(Hoyopass hoyopass) {
        LtuidLtoken ltuidLtoken = getLtuidLtoken(hoyopass);

        HoyoResponse<UserGameRoles> apiResponse = accountApi.getUserGameRoles(ltuidLtoken);
        List<UserGameRole> userGameRoles = apiResponse.getData().getList();

        List<Uid> uids = userGameRoles.stream()
                .map(ugr-> Uid.builder()
                        .uidString(ugr.getGameUid())
                        .characterLevel(ugr.getLevel())
                        .characterName(ugr.getNickname())
                        .region(Region.fromString(ugr.getRegion()))
                        .isLumine(containsLumine(ltuidLtoken, ugr.getGameUid(), ugr.getRegion()))
                        .build())
                .collect(Collectors.toList());

        return uids;
    }

    private LtuidLtoken getLtuidLtoken(Hoyopass hoyopass) {
        return converters.convert(hoyopass, LtuidLtoken.class);
    }

    /**
     * 해당 UID가 루미네를 갖고 있는지 미호요 API를 호출하여 판단합니다.
     * @param ltuidLtoken 미호요 통행증 쿠키
     * @param uid UID 스트링
     * @param region 해당 UID의 서버
     * @return 이 UID가 루미네를 캐릭터로 포함하고 있는지 여부
     */
    private boolean containsLumine(LtuidLtoken ltuidLtoken, String uid, String region) {
        try {
            HoyoResponse<GenshinAvatars> apiResponse = gameRecordApi.getAllAvartar(ltuidLtoken, uid, region);
            return apiResponse.getData().containsLumine();
        } catch (NotLoggedInError e) {
            e.printStackTrace();
            log.warn("This account cannot use HoYoLab api. Please create your nickname at HoYoLab main homepage.");
            return false;
        }
    }
}
