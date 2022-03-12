package org.binchoo.paimonganyu.hoyoapi.error.aspect;

import org.binchoo.paimonganyu.PaimonGanyuApp;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyoapi.pojo.GenshinAvatar;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.entity.utils.HoyopassUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {PaimonGanyuApp.class})
class RetcodeInspectionAspectTest {

    @Autowired
    HoyolabAccountApi accountApi;

    @Autowired
    HoyolabGameRecordApi gameRecordApi;

    Hoyopass hoyopass = Hoyopass.builder()
            .ltuid("77407897")
            .ltoken("rN0anFtemzPu8vfYLW0hkLkysJeidRk3vN6YGtjA")
            .build();

    @Test
    void getAllCharacter() {
        LtuidLtoken ltuidLtoken = HoyopassUtils.ltuidLtoken(hoyopass);

        for (UserGameRole gameRole : accountApi.getUserGameRoles(ltuidLtoken).getData().getList()) {
            System.out.println(gameRole);
            String uid = gameRole.getGameUid();
            String server = gameRole.getRegion();

            List<GenshinAvatar> avatars = gameRecordApi.getAllCharacter(ltuidLtoken, uid, server).getData().getAvatars();
            avatars.stream().filter(GenshinAvatar::isAether).findFirst()
                    .ifPresent(System.out::println);
        }
    }

    @Test
    void getGivenCharacters() {
        LtuidLtoken ltuidLtoken = HoyopassUtils.ltuidLtoken(hoyopass);
        int aether = 10000005;

        for (UserGameRole gameRole : accountApi.getUserGameRoles(ltuidLtoken).getData().getList()) {
            System.out.println(gameRole);
            String uid = gameRole.getGameUid();
            String server = gameRole.getRegion();

            try {
                List<GenshinAvatar> avatars = gameRecordApi.getCharacters(ltuidLtoken, uid, server, aether).getData().getAvatars();
                avatars.forEach(System.out::println);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void getDailyNote() {
        LtuidLtoken ltuidLtoken = HoyopassUtils.ltuidLtoken(hoyopass);

        for (UserGameRole gameRole : accountApi.getUserGameRoles(ltuidLtoken).getData().getList()) {
            System.out.println(gameRole);
            String uid = gameRole.getGameUid();
            String server = gameRole.getRegion();

            try{
                DailyNote note = gameRecordApi.getDailyNote(ltuidLtoken, uid, server).getData();
                System.out.println(note);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}