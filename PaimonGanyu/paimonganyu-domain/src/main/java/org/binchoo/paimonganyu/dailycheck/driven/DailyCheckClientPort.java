package org.binchoo.paimonganyu.dailycheck.driven;

import org.binchoo.paimonganyu.dailycheck.DailyCheckRequestResult;

public interface DailyCheckClientPort {

    DailyCheckRequestResult sendRequest(String ltuid, String ltoken);
}
