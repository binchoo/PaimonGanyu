package org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserDailyCheckDynamoAdapter implements UserDailyCheckCrudPort {

    @Override
    public UserDailyCheck save(UserDailyCheck queued) {
        return null;
    }

    @Override
    public List<UserDailyCheck> findByBotUserIdLtuid(String botUserId, String ltuid) {
        return null;
    }
}
