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

    public static FallbackMethod Home           = new FallbackMethod("Home");
    public static FallbackMethod ValidationCs   = new FallbackMethod("ValidationCs");
    public static FallbackMethod CommonCs       = new FallbackMethod("CommonCs");

    public static FallbackMethod ScanHoyopass   = new FallbackMethod("ScanHoyopass");
    public static FallbackMethod DeleteHoyopass = new FallbackMethod("DeleteHoyopass");
    public static FallbackMethod DeleteHoyopassGuide = new FallbackMethod("DeleteHoyopassGuide");
    public static FallbackMethod ListHoyopass   = new FallbackMethod("ListHoyopass");

    public static FallbackMethod ListTravelerStatus   = new FallbackMethod("ListTravelerStatus");

    public static FallbackMethod DailyCheckIn = new FallbackMethod("DailyCheckIn");
    public static FallbackMethod ListUserDailyCheck = new FallbackMethod("ListUserDailyCheck");

    private static Map<String, FallbackMethod> searchMap = new HashMap<>();

    static {
        for (Field field : FallbackMethods.class.getDeclaredFields()) {
            Class<?> cls = field.getType();
            boolean isFallbackMethod = cls.isAssignableFrom(FallbackMethod.class);
            if (isFallbackMethod && Modifier.isStatic(field.getModifiers())) {
                try {
                    FallbackMethod fm = (FallbackMethod) field.get(null);
                    FallbackMethods.searchMap.put(fm.getId(), fm);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to init FallbackMethods");
                }
            }
        }
    }

    private FallbackMethods() { }

    public static FallbackMethod findByBlockName(String blockName) {
        return searchMap.get(blockName);
    }
}
