package org.binchoo.paimonganyu.dailycheck.driven;

import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;

import java.util.List;

public interface UserDailyCheckCrudPort {

    UserDailyCheck save(UserDailyCheck queued);
    List<UserDailyCheck> findByBotUserIdLtuid(String botUserId, String ltuid);
    List<UserDailyCheck> findByBotUserIdLtuid(String botUserId, String ltuid, int count);
}
