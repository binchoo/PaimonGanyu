package org.binchoo.paimonganyu.infra.hoyopass.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.DataSwitchConfigurer;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.NotLoggedInError;
import org.binchoo.paimonganyu.hoyoapi.pojo.*;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// Too big.
@Slf4j
@RequiredArgsConstructor
@Component
public class UidSearchClientAdapter implements UidSearchClientPort {

    private final HoyolabAccountApi accountApi;
    private final HoyolabGameRecordApi gameRecordApi;
    private final DataSwitchConfigurer dataSwitchConfigurer;

    @Override
    public List<Uid> findUids(Hoyopass hoyopass) {
        LtuidLtoken ltuidLtoken = getLtuidLtoken(hoyopass);
        List<UserGameRole> userGameRoles = requestUserGameRoles(hoyopass, ltuidLtoken).getList();
        return mapUserGameRoleToUid(userGameRoles, ltuidLtoken);
    }

    /**
     * @param hoyopass 통행증 객체
     * @param ltuidLtoken API 전송 형식 통행증 크레덴셜
     * @throws IllegalArgumentException 통행증이 유효하지 않아 UID를 조회할 수 없었을 경우,
     * <p> API 응답에서 null 데이터가 담겨 {@link NullPointerException}을 받았을 경우.
     */
    private UserGameRoles requestUserGameRoles(Hoyopass hoyopass, LtuidLtoken ltuidLtoken) {
        try {
            return accountApi.getUserGameRoles(ltuidLtoken).getData();
        } catch (RetcodeException e) {
            throw new IllegalArgumentException(
                    String.format("UID를 조회할 수 없는 통행증입니다: %s", hoyopass), e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                    String.format("UID가 담겨있지 않은 통행증입니다: %s", hoyopass), e);
        }
    }

    private List<Uid> mapUserGameRoleToUid(List<UserGameRole> userGameRoles, LtuidLtoken ltuidLtoken) {
        return userGameRoles.stream()
                .map(ugr -> Uid.builder()
                        .uidString(ugr.getGameUid())
                        .characterLevel(ugr.getLevel())
                        .characterName(ugr.getNickname())
                        .region(Region.fromString(ugr.getRegion()))
                        .isLumine(requestContainsLumine(ugr, ltuidLtoken))
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
    private boolean requestContainsLumine(UserGameRole userGameRole, LtuidLtoken ltuidLtoken) {
        try {
            boolean proceed = dataSwitchConfigurer.turnOn(ltuidLtoken);
            if (proceed) {
                String uid = userGameRole.getGameUid();
                String region = userGameRole.getRegion();
                HoyoResponse<GenshinAvatars> apiResponse = gameRecordApi.getAllAvartars(ltuidLtoken, uid, region);
                return apiResponse.getData().containsLumine();
            }
        } catch (NotLoggedInError e) {
            log.warn("주어진 통행증은 HoYoLab API를 이용할 수 없었습니다. 이 통행증에 대해 HoYoLab 닉네임 설정 여부 확인이 필요합니다.", e);
        }
        return false;
    }
}
