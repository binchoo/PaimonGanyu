package org.binchoo.paimonganyu.dailycheck.app;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.dailycheck.domain.DailyCheckTaskSpec;
import org.binchoo.paimonganyu.dailycheck.domain.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.domain.driving.DailyCheckPort;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DailyCheckApp implements DailyCheckPort {

    private final UserDailyCheckCrudPort userDailyCheckCrudPort;

    @Override
    public void request(DailyCheckTaskSpec dailyChecktask) {

    }

    @Override
    public boolean isDoneToday(DailyCheckTaskSpec dailyCheckTaskSpec) {
        return false;
    }
}
