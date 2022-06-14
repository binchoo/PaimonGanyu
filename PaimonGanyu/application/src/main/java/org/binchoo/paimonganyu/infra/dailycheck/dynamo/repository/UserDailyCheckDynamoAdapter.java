package org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.item.UserDailyCheckItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserDailyCheckDynamoAdapter implements UserDailyCheckCrudPort {

    private final UserDailyCheckDynamoRepository dynamoRepository;

    @Override
    public UserDailyCheck save(UserDailyCheck userDailyCheck) {
        return UserDailyCheckItem.toDomain(dynamoRepository.save(UserDailyCheckItem.fromDomain(userDailyCheck)));
    }

    @Override
    public List<UserDailyCheck> findByBotUserIdLtuid(String botUserId, String ltuid) {
        String id = concat(botUserId, ltuid);
        return dynamoRepository.findByBotUserIdLtuid(id).stream()
                .map(UserDailyCheckItem::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDailyCheck> findByBotUserIdLtuid(String botUserId, String ltuid, int count) {
        String id = concat(botUserId, ltuid);
        PageRequest pageRequest =
                PageRequest.of(0, (int) count, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<UserDailyCheckItem> result = dynamoRepository.findByBotUserIdLtuid(id, pageRequest);
        return result.stream().map(UserDailyCheckItem::toDomain)
                .collect(Collectors.toList());
    }

    private String concat(String botUserId, String ltuid) {
        return botUserId + "-" + ltuid;
    }
}
