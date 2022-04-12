package org.binchoo.paimonganyu.dailycheck.domain.driven;

import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheck;

import java.util.Collection;
import java.util.List;

public interface UserDailyCheckCrudPort {
    UserDailyCheck save(UserDailyCheck queued);

    List<UserDailyCheck> findByBotUserIdLtuid(String botUserId, String ltuid);
}
