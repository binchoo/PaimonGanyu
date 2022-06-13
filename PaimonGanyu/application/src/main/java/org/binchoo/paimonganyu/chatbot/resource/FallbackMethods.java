package org.binchoo.paimonganyu.chatbot.resource;

import org.binchoo.paimonganyu.error.FallbackMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
public final class FallbackMethods {

    public static FallbackMethod Home           = new FallbackMethod("Home");
    public static FallbackMethod DeleteHoyopass = new FallbackMethod("DeleteHoyopass");
    public static FallbackMethod ScanHoyopass   = new FallbackMethod("ScanHoyopass");
    public static FallbackMethod ValidationCs   = new FallbackMethod("ValidationCs");
    public static FallbackMethod CommonCs       = new FallbackMethod("CommonCs");
    public static Map<String, FallbackMethod> searchMap = new HashMap<>();
    static {
        searchMap.put(FallbackMethods.Home.getId(), FallbackMethods.Home);
        searchMap.put(FallbackMethods.DeleteHoyopass.getId(), FallbackMethods.DeleteHoyopass);
        searchMap.put(FallbackMethods.ScanHoyopass.getId(), FallbackMethods.ScanHoyopass);
        searchMap.put(FallbackMethods.ValidationCs.getId(), FallbackMethods.ValidationCs);
        searchMap.put(FallbackMethods.CommonCs.getId(), FallbackMethods.CommonCs);
    }

    private FallbackMethods() { }

    public static FallbackMethod get(String blockName) {
        return searchMap.get(blockName);
    }
}
