package org.binchoo.paimonganyu.chatbot.error.support;

import org.binchoo.paimonganyu.error.FallbackMethod;

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

    private FallbackMethods() { }
}
