package org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.item.UserDailyCheckItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserDailyCheckDynamoAdapter implements UserDailyCheckCrudPort {

    private final UserDailyCheckDynamoRepository repository;

    @Override
    public UserDailyCheck save(UserDailyCheck userDailyCheck) {
        return UserDailyCheckItem.toDomain(repository.save(UserDailyCheckItem.fromDomain(userDailyCheck)));
    }

    @Override
    public List<UserDailyCheck> findByBotUserIdLtuid(String botUserId, String ltuid) {
        final String botUserIdLtuid = botUserId + "-" + ltuid;
        return repository.findByBotUserIdLtuid(botUserIdLtuid).stream()
                .map(UserDailyCheckItem::toDomain)
                .collect(Collectors.toList());
    }
}
