package org.binchoo.paimonganyu.dailycheck.domain.driving;

import org.binchoo.paimonganyu.dailycheck.domain.DailyCheckTaskSpec;

public interface DailyCheckPort {

    void request(DailyCheckTaskSpec dailyChecktask);
    boolean checkDuplicate(DailyCheckTaskSpec dailyCheckTaskSpec);
}
