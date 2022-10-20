package org.binchoo.paimonganyu.hoyopass.driving;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;

/**
 * @author jbinchoo
 * @since 2022/10/20
 */
public interface HoyopassSyncPort {

    UserHoyopass synchronize(UserHoyopass old);

    Hoyopass synchronize(Hoyopass old);
}
