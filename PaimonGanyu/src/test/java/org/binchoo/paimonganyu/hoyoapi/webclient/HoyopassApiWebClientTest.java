//package org.binchoo.paimonganyu.hoyoapi.webclient;
//
//import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
//import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
//import org.binchoo.paimonganyu.hoyoapi.pojo.UserGameRole;
//import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;
//import org.binchoo.paimonganyu.hoyopass.entity.Uid;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//class HoyopassApiWebClientTest {
//
//    HoyolabAccountApi client = new HoyolabAccountWebClient();
//    Hoyopass hoyopass = Hoyopass.builder()
//            .ltuid("77407897")
//            .ltoken("rN0anFtemzPu8vfYLW0hkLkysJeidRk3vN6YGtjA")
//            .build();
//
//    @Test
//    void getUserGameRoles() {
//        LtuidLtoken ltuidLtoken = new LtuidLtoken(hoyopass.getLtuid(), hoyopass.getLtoken());
//        List<UserGameRole> list = client.getUserGameRoles(ltuidLtoken);
//        for (UserGameRole userGameRole : list) {
//            System.out.println(userGameRole.getGameUid());
//            System.out.println(userGameRole.getRegion());
//            System.out.println(userGameRole.getNickname());
//        }
//    }
//
//    @Test
//    void getUserGameRoleByRegion() {
//        LtuidLtoken ltuidLtoken = new LtuidLtoken(hoyopass.getLtuid(), hoyopass.getLtoken());
//        UserGameRole userGameRole = client.getUserGameRoleByRegion(ltuidLtoken, Uid.Region.OS_USA.lowercase());
//        System.out.println(userGameRole.getGameUid());
//        System.out.println(userGameRole.getRegion());
//        System.out.println(userGameRole.getNickname());
//    }
//}