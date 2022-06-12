package org.binchoo.paimonganyu.chatbot.error.support;

import org.binchoo.paimonganyu.error.FallbackId;

/**
 * @author : jbinchoo
 * @since : 2022-06-12
 */
public class Fallbacks {

    public static FallbackId Home           = new FallbackId("Home");
    public static FallbackId DeleteHoyopass = new FallbackId("DeleteHoyopass");
    public static FallbackId ScanHoyopass   = new FallbackId("ScanHoyopass");
    public static FallbackId ValidationCs   = new FallbackId("ValidationCs");
    public static FallbackId CommonCs       = new FallbackId("CommonCs");

    private Fallbacks() { }
}
