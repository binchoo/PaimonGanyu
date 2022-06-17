package org.binchoo.paimonganyu.chatbot.errorbinders;

import org.binchoo.paimonganyu.error.FallbackMethod;

/**
 * @author : jbinchoo
 * @since : 2022-06-17
 */
public class DailyCheckExceptionBinder extends HoyopassExceptionBinder {

    DailyCheckExceptionBinder(Class<?> error, String title, FallbackMethod[] fallbacks) {
        super(error, title, fallbacks);
    }
}
