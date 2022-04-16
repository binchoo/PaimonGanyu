package org.binchoo.paimonganyu.testfixture.hoyopass;

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
public class HoyopassMockUtils {

    private static final Random random = new Random();

    private HoyopassMockUtils() { }

    public static UserHoyopass getMockUserHoyopass() {
        String botUserId = generateRandom();
        return UserHoyopass.builder()
                .botUserId(botUserId)
                .hoyopasses(Arrays.asList(getMockHoyopass(), getMockHoyopass()))
                .build();
    }

    public static Hoyopass getMockHoyopass() {
        String ltoken = generateRandom();
        String ltuid = generateRandom();
        return Hoyopass.builder()
                .ltoken(ltoken)
                .ltuid(ltuid)
                .uids(Arrays.asList(getMockUid(), getMockUid()))
                .build();
    }

    public static Uid getMockUid() {
        String characterName = "MockCharacterName-" + generateRandom();
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

    private static String generateRandom() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
