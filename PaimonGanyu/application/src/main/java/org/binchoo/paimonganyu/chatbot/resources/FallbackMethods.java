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

    private static Map<String, FallbackMethod> nameCache = new HashMap<>();

    static {
        for (Field field : FallbackMethods.class.getDeclaredFields())
            assignFallbackMethod(field);
    }

    private static void assignFallbackMethod(Field field) {
        Class<?> cls = field.getType();
        boolean isFallbackMethod = cls.isAssignableFrom(FallbackMethod.class);
        boolean isStaticField = Modifier.isStatic(field.getModifiers());
        if (isFallbackMethod && isStaticField) {
            String fallbackName = field.getName();
            FallbackMethod fallback = new FallbackMethod(fallbackName);
            try {
                field.set(null, fallback);
                nameCache.put(fallbackName, fallback);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static FallbackMethod findByName(String name) {
        return nameCache.get(name);
    }

    private FallbackMethods() { }
}
