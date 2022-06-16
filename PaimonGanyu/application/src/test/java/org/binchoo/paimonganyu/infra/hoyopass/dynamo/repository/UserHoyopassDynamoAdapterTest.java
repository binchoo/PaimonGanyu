package org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.item.UserHoyopassItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils.getMockUserHoyopass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author : jbinchoo
 * @since : 2022-04-15
 */
@ExtendWith(MockitoExtension.class)
class UserHoyopassDynamoAdapterTest {

    @Mock
    UserHoyopassDynamoRepository repository;

    @InjectMocks
    UserHoyopassDynamoAdapter userHoyopassDynamoAdapter;

    @Test
    void withNoEntry_findAll_returnsEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<UserHoyopass> result = userHoyopassDynamoAdapter.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void withSomeEntries_findAll_returnsEveryEntry() {
        int entryCount = 20;
        var mockUserHoyopassList = IntStream.range(0, entryCount)
                .mapToObj(i-> getMockUserHoyopass())
                .collect(Collectors.toList());

        when(repository.findAll()).thenReturn(mockUserHoyopassList.stream()
                .map(UserHoyopassItem::fromDomain)
                .collect(Collectors.toList()));

        List<UserHoyopass> result = userHoyopassDynamoAdapter.findAll();
        assertThat(result).hasSize(entryCount);
        mockUserHoyopassList.forEach(it-> assertThat(it).isIn(result));
    }

    @Test
    void withoutMatchingEntry_findByBotUserId_returnsEmptyOptional() {
        when(repository.findByBotUserId(any())).thenReturn(Optional.empty());

        var result = userHoyopassDynamoAdapter.findByBotUserId(RandomString.make());
        assertThat(result).isNotPresent();
    }

    @Test
    void withMatchingEntry_findByBotUserId_returnsPresentOptional() {
        var mockUserHoyopass = getMockUserHoyopass();
        var mockUserHoyopassItem = UserHoyopassItem.fromDomain(mockUserHoyopass);

        when(repository.findByBotUserId(mockUserHoyopassItem.getBotUserId()))
                .thenReturn(Optional.of(mockUserHoyopassItem));

        Optional<UserHoyopass> result = userHoyopassDynamoAdapter.findByBotUserId(mockUserHoyopass.getBotUserId());
        assertThat(result)
                .isPresent()
                .contains(mockUserHoyopass);
    }

    @Test
    void save() {
        var userHoyopass = getMockUserHoyopass();
        var userHoyopassItem = UserHoyopassItem.fromDomain(userHoyopass);

        when(repository.save(any())).thenReturn(userHoyopassItem);

        UserHoyopass result = userHoyopassDynamoAdapter.save(userHoyopass);
        assertThat(result).isEqualTo(userHoyopass);
    }

    @Test
    void withoutMatchingEntry_existsByBotUserId_returnsFalse() {
        var userHoyopass = getMockUserHoyopass();

        when(repository.existsByBotUserId(userHoyopass.getBotUserId()))
                .thenReturn(false);

        boolean result = userHoyopassDynamoAdapter.existsByBotUserId(userHoyopass.getBotUserId());
        assertThat(result).isFalse();
    }

    @Test
    void withMatchingEntry_existsByBotUserId_returnsFalse() {
        var userHoyopass = getMockUserHoyopass();

        when(repository.existsByBotUserId(userHoyopass.getBotUserId()))
                .thenReturn(true);

        boolean result = userHoyopassDynamoAdapter.existsByBotUserId(userHoyopass.getBotUserId());
        assertThat(result).isTrue();
    }
}