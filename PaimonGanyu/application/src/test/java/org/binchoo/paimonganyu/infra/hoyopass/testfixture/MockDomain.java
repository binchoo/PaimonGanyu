package org.binchoo.paimonganyu.infra.hoyopass.testfixture;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

import java.util.Arrays;
import java.util.Random;

/**
 * @author : jbinchoo
 * @since : 2022-04-15
 */
public class MockDomain {

    private MockDomain() { }

    public static UserHoyopass getMockUserHoyopass() {
        String botUserId = RandomString.make();
        return UserHoyopass.builder()
                .botUserId(botUserId)
                .hoyopasses(Arrays.asList(getMockHoyopass(), getMockHoyopass()))
                .build();
    }

    public static Hoyopass getMockHoyopass() {
        String ltoken = RandomString.make();
        String ltuid = RandomString.make();
        return Hoyopass.builder()
                .ltoken(ltoken)
                .ltuid(ltuid)
                .uids(Arrays.asList(getMockUid(), getMockUid()))
                .build();
    }

    public static Uid getMockUid() {
        String characterName = "CharacterName_" + RandomString.make();
        Random rand = new Random();
        boolean isLumie = rand.nextBoolean();
        int characterLevel = rand.nextInt();
        int regionIdx = Math.abs(rand.nextInt()) % Region.values().length;
        return Uid.builder()
                .characterName(characterName)
                .isLumine(isLumie)
                .characterLevel(characterLevel)
                .region(Region.values()[regionIdx])
                .build();
    }
}
