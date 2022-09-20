package org.binchoo.paimonganyu.chatbot.resources;

import org.binchoo.paimonganyu.error.FallbackMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 코드에서 챗봇 블록의 논리적 이름을 String 타입으로 다루면 실수하기 쉽습니다.
 * <p> {@code FallbackMethods} 싱글톤은 블록들의 논리적 이름과 완전 동일한 이름의 공개 필드들을 제공합니다.
 * <p> 스킬 응답 뷰들은 챗봇 블록의 논리적 이름을 String으로 다루지 않아야 하며, 예) "CommonCs"
 * <p> 대신 {@code FallbackMethods.CommonCs}처럼 이곳의 퍼블릭 필드를 활용해야 합니다.
 * <p> 자바는 스크립트 언어가 아니기 때문에 채용 가능한 방식입니다.
 * <p> 관리할 챗봇 블록이 신규될 경우, 이곳에 동일한 이름의 공개 필드를 추가하기 바랍니다.
 * @author jbinchoo
 * @since 2022-06-12
 */
public final class FallbackMethods {

    public static FallbackMethod Home;
    public static FallbackMethod ValidationCs;
    public static FallbackMethod CommonCs;

    public static FallbackMethod ScanHoyopass;
    public static FallbackMethod ScanHoyopassGuide;
    public static FallbackMethod DeleteHoyopass;
    public static FallbackMethod ListHoyopass;
    public static FallbackMethod ListHoyopassAliasDeleteHoyopass;

    public static FallbackMethod ListTravelerStatus;

    public static FallbackMethod DailyCheckIn;
    public static FallbackMethod ListUserDailyCheck;

    public static FallbackMethod ListUserRedeem;
    public static FallbackMethod ListUserRedeemText;

    private static Map<String, FallbackMethod> fallbackMethods = new HashMap<>();

    static {
        for (Field field : FallbackMethods.class.getDeclaredFields())
            assignFallbackMethod(field);
    }

    /**
     * 리플렉션으로 공개 필드의 이름이 표상하는 대체 수단 객체를 할당합니다.
     * @param field 공개 필드
     */
    private static void assignFallbackMethod(Field field) {
        Class<?> cls = field.getType();
        boolean isFallbackMethod = cls.isAssignableFrom(FallbackMethod.class);
        boolean isStaticField = Modifier.isStatic(field.getModifiers());
        if (isFallbackMethod && isStaticField) {
            String blockName = field.getName();
            FallbackMethod fallback = new FallbackMethod(blockName);
            try {
                field.set(null, fallback);
                fallbackMethods.put(blockName, fallback);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static FallbackMethod of(String name) {
        return fallbackMethods.get(name);
    }

    private FallbackMethods() { }
}
