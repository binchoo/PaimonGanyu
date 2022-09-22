package org.binchoo.paimonganyu.testfixture.hoyopass;

import org.binchoo.paimonganyu.hoyopass.*;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : jbinchoo
 * @since : 2022-04-15
 */
public class HoyopassMockUtils {

    private static final Random random = new Random();

    private HoyopassMockUtils() { }

    public static UserHoyopass mockUserHoyopass() {
        return mockUserHoyopass(genRandom());
    }

    public static UserHoyopass mockUserHoyopass(String botUserId) {
        return mockUserHoyopass(botUserId, 2);
    }

    public static UserHoyopass mockUserHoyopass(String botUserId, int size) {
        return UserHoyopass.builder()
                .botUserId(botUserId)
                .hoyopasses(IntStream.range(0, size)
                        .mapToObj(i-> mockHoyopass())
                        .collect(Collectors.toList()))
                .build();
    }

    public static Hoyopass mockHoyopass() {
        return mockHoyopass(4);
    }

    public static Hoyopass mockHoyopass(int size) {
        String ltoken = genRandom();
        String ltuid = genRandom();
        String cookieToken = genRandom();
        return Hoyopass.builder()
                .credentials(HoyopassCredentials.builder()
                        .ltuid(ltuid)
                        .ltoken(ltoken)
                        .cookieToken(cookieToken)
                        .build())
                .uids(IntStream.range(0, size)
                        .mapToObj(i-> mockUid())
                        .collect(Collectors.toList()))
                .build();
    }

    public static Uid mockUid() {
        String uidString = "MockUidString-" + genRandom();
        String characterName = "MockCharacterName-" + genRandom();
        Random rand = new Random();
        boolean isLumie = rand.nextBoolean();
        int characterLevel = rand.nextInt();
        int regionIdx = Math.abs(rand.nextInt()) % Region.values().length;
        return Uid.builder()
                .uidString(uidString)
                .characterName(characterName)
                .isLumine(isLumie)
                .characterLevel(characterLevel)
                .region(Region.values()[regionIdx])
                .build();
    }

    private static String genRandom() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
