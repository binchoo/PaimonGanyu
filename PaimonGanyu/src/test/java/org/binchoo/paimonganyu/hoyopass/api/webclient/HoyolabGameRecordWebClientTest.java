package org.binchoo.paimonganyu.hoyopass.api.webclient;

import org.binchoo.paimonganyu.hoyopass.api.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyopass.api.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyopass.api.pojo.DailyNote;
import org.binchoo.paimonganyu.hoyopass.api.pojo.GenshinAvatar;
import org.binchoo.paimonganyu.hoyopass.api.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.api.pojo.UserGameRole;
import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.entity.utils.HoyopassUtils;
import org.junit.jupiter.api.Test;

import java.util.List;


class HoyolabGameRecordWebClientTest {

    HoyolabAccountApi accountClient = new HoyolabAccountWebClient();
    HoyolabGameRecordApi bbsClient = new HoyolabGameRecordWebClient();

    Hoyopass hoyopass = Hoyopass.builder()
            .ltuid("77407897")
            .ltoken("rN0anFtemzPu8vfYLW0hkLkysJeidRk3vN6YGtjA")
            .build();

    @Test
    void getAllCharacter() {
        LtuidLtoken ltuidLtoken = HoyopassUtils.ltuidLtoken(hoyopass);

        for (UserGameRole gameRole : accountClient.getUserGameRoles(ltuidLtoken)) {
            System.out.println(gameRole);
            String uid = gameRole.getGameUid();
            String server = gameRole.getRegion();

            List<GenshinAvatar> avatars = bbsClient.getAllCharacter(ltuidLtoken, uid, server);
            avatars.stream().filter(GenshinAvatar::isAether).findFirst()
                    .ifPresent(System.out::println);
        }
    }

    @Test
    void getGivenCharacters() {
        LtuidLtoken ltuidLtoken = HoyopassUtils.ltuidLtoken(hoyopass);
        int aether = 10000005;

        for (UserGameRole gameRole : accountClient.getUserGameRoles(ltuidLtoken)) {
            System.out.println(gameRole);
            String uid = gameRole.getGameUid();
            String server = gameRole.getRegion();

            try {
                List<GenshinAvatar> avatars = bbsClient.getCharacters(ltuidLtoken, uid, server, aether);
                avatars.forEach(System.out::println);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void getDailyNote() {
        LtuidLtoken ltuidLtoken = HoyopassUtils.ltuidLtoken(hoyopass);

        for (UserGameRole gameRole : accountClient.getUserGameRoles(ltuidLtoken)) {
            System.out.println(gameRole);
            String uid = gameRole.getGameUid();
            String server = gameRole.getRegion();

            try{
                DailyNote note = bbsClient.getDailyNote(ltuidLtoken, uid, server);
                System.out.println(note);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}