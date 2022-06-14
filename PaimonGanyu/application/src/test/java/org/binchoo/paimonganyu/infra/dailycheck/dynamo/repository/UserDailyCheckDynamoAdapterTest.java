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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDailyCheckDynamoAdapterTest {

    @Mock
    UserDailyCheckDynamoRepository repository;

    @InjectMocks
    UserDailyCheckDynamoAdapter dynamoAdapter;

    @Test
    void save() {
        UserDailyCheck original = completedState();
        UserDailyCheckItem item = convertToItem(original);

        when(repository.save(any())).thenReturn(item);

        UserDailyCheck saved = dynamoAdapter.save(original);

        assertThat(saved.getLtoken()).isNull();
        assertEquals(original, saved);
    }

    @Test
    void findByBotUserIdLtuid() {
        UserDailyCheck original = completedState();
        String id = botUserIdLtuid(original);

        // Assume that ltuid and botUserid are null, because dynamodb table will not persist those attributes
        UserDailyCheckItem item = convertToItem(original);
        item.setLtuid(null);
        item.setBotUserId(null);

        when(repository.findByBotUserIdLtuid(id)).thenReturn(
                Collections.singletonList(item));

        List<UserDailyCheck> found = dynamoAdapter
                .findByBotUserIdLtuid(original.getBotUserId(), original.getLtuid());

        found.stream().forEach(it-> {
            assertThat(it.getLtoken()).isNull();
            assertEquals(original, it);
        });
    }

    private UserDailyCheck completedState() {
        String random = RandomString.make();
        return UserDailyCheck.builder()
                .botUserId(random)
                .ltuid(random)
                .ltoken(random)
                .status(UserDailyCheckStatus.COMPLETED)
                .build();
    }

    private UserDailyCheckItem convertToItem(UserDailyCheck original) {
        return UserDailyCheckItem.fromDomain(original);
    }

    private String botUserIdLtuid(UserDailyCheck original) {
        return original.getBotUserId() + "-" + original.getLtuid();
    }

    private void assertEquals(UserDailyCheck original, UserDailyCheck it) {
        assertThat(it.getBotUserId()).isEqualTo(original.getBotUserId());
        assertThat(it.getLtuid()).isEqualTo(original.getLtuid());

        assertThat(it.getStatus()).isEqualTo(original.getStatus());
        assertThat(it.getTimestamp()).isEqualTo(original.getTimestamp());
    }
}