package org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository;

import net.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckStatus;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.item.UserDailyCheckItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDailyCheckDynamoAdapterTest {

    @Mock
    UserDailyCheckDynamoRepository repository;

    @InjectMocks
    UserDailyCheckDynamoAdapter userDailyCheckDynamoAdapter;

    @Test
    void save() {
        UserDailyCheck originalEntity = getCompletedUserDailyCheck();
        UserDailyCheckItem item = UserDailyCheckItem.fromDomain(originalEntity);

        when(repository.save(any())).thenReturn(item);

        UserDailyCheck saved = userDailyCheckDynamoAdapter.save(originalEntity);
        assertThat(saved.getBotUserId()).isEqualTo(originalEntity.getBotUserId());
        assertThat(saved.getLtuid()).isEqualTo(originalEntity.getLtuid());
        assertThat(saved.getLtoken()).isNull();
        assertThat(saved.getStatus()).isEqualTo(originalEntity.getStatus());
        assertThat(saved.getTimestamp()).isEqualTo(originalEntity.getTimestamp());
    }

    private UserDailyCheck getCompletedUserDailyCheck() {
        String random = RandomString.make();
        return UserDailyCheck.builder()
                .botUserId(random)
                .ltuid(random)
                .ltoken(random)
                .status(UserDailyCheckStatus.COMPLETED)
                .build();
    }

    @Test
    void findByBotUserIdLtuid() {
        UserDailyCheck originalEntity = getCompletedUserDailyCheck();
        UserDailyCheckItem sameItem = UserDailyCheckItem.fromDomain(originalEntity);
        // assume that ltuid and botuserid are null, because dynamodb table will not persist those attributes
        sameItem.setLtuid(null);
        sameItem.setBotUserId(null);
        String botUserIdLtuid = originalEntity.getBotUserId() + "-" + originalEntity.getLtuid();

        when(repository.findByBotUserIdLtuid(botUserIdLtuid)).thenReturn(
                Collections.singletonList(sameItem));

        List<UserDailyCheck> found = userDailyCheckDynamoAdapter
                .findByBotUserIdLtuid(originalEntity.getBotUserId(), originalEntity.getLtuid());

        found.stream().forEach(it-> {
            assertThat(it.getBotUserId()).isEqualTo(originalEntity.getBotUserId());
            assertThat(it.getLtuid()).isEqualTo(originalEntity.getLtuid());
            assertThat(it.getLtoken()).isNull();
            assertThat(it.getStatus()).isEqualTo(originalEntity.getStatus());
            assertThat(it.getTimestamp()).isEqualTo(originalEntity.getTimestamp());
        });
    }
}