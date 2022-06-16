package org.binchoo.paimonganyu.chatbot.resources;

import org.binchoo.paimonganyu.error.FallbackMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
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

    private static Map<String, FallbackMethod> searchMap = new HashMap<>();

    static {
        for (Field field : FallbackMethods.class.getDeclaredFields())
            assignFallbackMethod(field);
    }

    private static void assignFallbackMethod(Field field) {
        Class<?> cls = field.getType();
        boolean isFallbackMethod = cls.isAssignableFrom(FallbackMethod.class);
        boolean isStaticField = Modifier.isStatic(field.getModifiers());
        if (isFallbackMethod && isStaticField) {
            String fallbackMethodName = field.getName();
            FallbackMethods.searchMap.put(fallbackMethodName, new FallbackMethod(fallbackMethodName));
        }
    }

    public static FallbackMethod findByBlockName(String blockName) {
        return searchMap.get(blockName);
    }

    private FallbackMethods() { }
}
