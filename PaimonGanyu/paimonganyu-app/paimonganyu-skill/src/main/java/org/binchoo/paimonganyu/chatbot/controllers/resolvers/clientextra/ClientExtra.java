package org.binchoo.paimonganyu.chatbot.controllers.resolvers.clientextra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jbinchoo
 * @since 2022/06/11
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientExtra {

    String value();
}
