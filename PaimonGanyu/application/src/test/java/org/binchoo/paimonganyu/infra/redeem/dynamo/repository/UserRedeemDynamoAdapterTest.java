package org.binchoo.paimonganyu.infra.redeem.dynamo.repository;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.UserRedeemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@ExtendWith(MockitoExtension.class)
class UserRedeemDynamoAdapterTest {

    @Mock
    UserRedeemDynamoRepository repository;

    @InjectMocks
    UserRedeemDynamoAdapter userRedeemDynamoAdapter;

    @DisplayName("완수 리딤 이력을 색인할 경우, 완수 그룹 상태들을 대입하여 색인한다")
    @Test
    void givenDoneUserRedeem_findMatches_searchesWithDoneGroupStatuses() {
        var userRedeem = givenDoneUserRedeem();
        userRedeemDynamoAdapter.findMatches(userRedeem);

        verify(repository).findByBotUserIdAndLtuidAndCodeAndStatusIn(
                userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode().getCode(),
                UserRedeemStatus.groupOfDone);
    }

    @DisplayName("완수가 아닌 리딤 이력을 색인할 경우, 완수 아님 그룹의 상태들을 대입하여 색인한다")
    @Test
    void givenNotDoneUserRedeem_findMatches_searchesWithNotDoneGroupStatuses() {
        var userRedeem = givenNotDoneUserRedeem();
        userRedeemDynamoAdapter.findMatches(userRedeem);

        verify(repository).findByBotUserIdAndLtuidAndCodeAndStatusIn(
                userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode().getCode(),
                UserRedeemStatus.groupOfNotDone);
    }

    @DisplayName("완수 리딤 이력의 존재 여부를 색인할 경우, 완수 그룹의 상태들을 대입하여 색인한다")
    @Test
    void givenDoneUserRedeem_existMatches_searchesWithDoneGroupStatuses() {
        var userRedeem = givenDoneUserRedeem();
        userRedeemDynamoAdapter.existMatches(userRedeem);

        verify(repository).existsByBotUserIdAndLtuidAndCodeAndStatusIn(
                userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode().getCode(),
                UserRedeemStatus.groupOfDone);
    }

    @DisplayName("완수가 아닌 리딤 이력의 존재 여부를 색인할 경우, 완수 아님 그룹의 상태들을 대입하여 색인한다")
    @Test
    void givenNotDoneUserRedeem_existMatches_searchesWithNotDoneGroupStatuses() {
        var userRedeem = givenNotDoneUserRedeem();
        userRedeemDynamoAdapter.existMatches(userRedeem);

        verify(repository).existsByBotUserIdAndLtuidAndCodeAndStatusIn(
                userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode().getCode(),
                UserRedeemStatus.groupOfNotDone);
    }

    @DisplayName("리딤 코드 기준으로 색인할 경우, 리딤 코드를 대입하여 색인한다")
    @Test
    void testFindByRedeemCode() {
        var userRedeem = randomUserRedeem();
        var redeemCode = userRedeem.getRedeemCode();
        userRedeemDynamoAdapter.findByRedeemCode(redeemCode);

        verify(repository).findByCode(redeemCode);
    }

    @DisplayName("모든 리딤 이력을 색인할 경우, 모든 리딤 이력에 대해 색인한 결과를 반환한다")
    @Test
    void testFindAll() {
        var mockFindAllResult = new ArrayList<UserRedeemItem>();
        for(int i = 0; i < 100; i++) mockFindAllResult.add(randomUserRedeemItem());
        when(repository.findAll()).thenReturn(mockFindAllResult);

        var findAllResult = userRedeemDynamoAdapter.findAll();

        verify(repository).findAll();
        assertThat(findAllResult).hasSameSizeAs(mockFindAllResult);
        assertThat(findAllResult).containsAll(
                mockFindAllResult.stream()
                        .map(UserRedeemItem::toDomain)
                        .collect(Collectors.toList()));
    }

    @DisplayName("이력을 저장할 경우, 대응되는 아이템으로 변환해서 이력을 저장한다")
    @Test
    void save() {
        var userRedeem = randomUserRedeem();
        when(repository.save(any())).thenReturn(UserRedeemItem.fromDomain(userRedeem));

        var saved = userRedeemDynamoAdapter.save(userRedeem);

        assertThat(saved).isEqualTo(userRedeem);
    }

    private UserRedeem givenDoneUserRedeem() {
        var userRedeem = randomUserRedeem();
        userRedeem.assumeDone();
        return userRedeem;
    }

    private UserRedeem givenNotDoneUserRedeem() {
        return randomUserRedeem();
    }

    private UserRedeem randomUserRedeem() {
        var random = RandomString.make();
        return new UserRedeem(random, random, new RedeemCode(random));
    }

    private UserRedeemItem randomUserRedeemItem() {
        return UserRedeemItem.fromDomain(randomUserRedeem());
    }
}