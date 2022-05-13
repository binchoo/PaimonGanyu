package org.binchoo.paimonganyu.infra.redeem.dynamo.repository;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("완수 리딤 이력을 색인할 경우, done=true 상태를 대입하여 색인한다")
    @Test
    void givenDoneUserRedeem_findMatches_searchesWithDoneGroupStatuses() {
        var userRedeem = givenDoneUserRedeem();
        userRedeemDynamoAdapter.findMatches(userRedeem);

        verify(repository).findByBotUserIdAndLtuidAndCodeAndDone(
                userRedeem.getBotUserId(), userRedeem.getLtuid(),
                userRedeem.getRedeemCode().getCode(), true);
    }

    @DisplayName("완수가 아닌 리딤 이력을 색인할 경우, done=false 상태 대입하여 색인한다")
    @Test
    void givenNotDoneUserRedeem_findMatches_searchesWithNotDoneGroupStatuses() {
        var userRedeem = givenNotDoneUserRedeem();
        userRedeemDynamoAdapter.findMatches(userRedeem);

        verify(repository).findByBotUserIdAndLtuidAndCodeAndDone(
                userRedeem.getBotUserId(), userRedeem.getLtuid(),
                userRedeem.getRedeemCode().getCode(),false);
    }

    @DisplayName("완수 리딤 이력의 존재 여부를 색인할 경우, done=true 상태를 대입하여 색인한다")
    @Test
    void givenDoneUserRedeem_existMatches_searchesWithDoneGroupStatuses() {
        var userRedeem = givenDoneUserRedeem();
        userRedeemDynamoAdapter.existMatches(userRedeem);

        verify(repository).existsByBotUserIdAndLtuidAndCodeAndDone(
                userRedeem.getBotUserId(), userRedeem.getLtuid(),
                userRedeem.getRedeemCode().getCode(), true);
    }

    @DisplayName("완수가 아닌 리딤 이력의 존재 여부를 색인할 경우, 완수 아님 그룹의 상태들을 대입하여 색인한다")
    @Test
    void givenNotDoneUserRedeem_existMatches_searchesWithNotDoneGroupStatuses() {
        var userRedeem = givenNotDoneUserRedeem();
        userRedeemDynamoAdapter.existMatches(userRedeem);

        verify(repository).existsByBotUserIdAndLtuidAndCodeAndDone(
                userRedeem.getBotUserId(), userRedeem.getLtuid(),
                userRedeem.getRedeemCode().getCode(), false);
    }

    @DisplayName("리딤 코드 기준으로 색인할 경우, 리딤 코드를 대입하여 색인한다")
    @Test
    void testFindByRedeemCode() {
        var userRedeem = randomUserRedeem();
        var redeemCode = userRedeem.getRedeemCode();
        var codeString = redeemCode.getCode();
        userRedeemDynamoAdapter.findByRedeemCode(redeemCode);

        verify(repository).findByCode(codeString);
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

    @DisplayName("이력을 저장할 경우, 반환되는 이력은 입력과 동등하다")
    @Test
    void testSave() {
        var userRedeem = randomUserRedeem();
        when(repository.save(any())).thenReturn(UserRedeemItem.fromDomain(userRedeem));

        var saved = userRedeemDynamoAdapter.save(userRedeem);

        assertThat(saved).isEqualTo(userRedeem);
    }

    @DisplayName("이력을 배치 저장할 경우, 반환되는 이력은 입력과 동등하다")
    @Test
    void testSaveAll() {
        var mockSaveAllResult = new ArrayList<UserRedeem>();
        for(int i = 0; i < 100; i++) mockSaveAllResult.add(randomUserRedeem());

        when(repository.saveAll(any())).thenReturn(mockSaveAllResult.stream()
                .map(UserRedeemItem::fromDomain).collect(Collectors.toList()));

        var saveAllResult = userRedeemDynamoAdapter.saveAll(mockSaveAllResult);

        assertThat(saveAllResult).hasSameSizeAs(mockSaveAllResult);
        assertThat(saveAllResult).containsAll(mockSaveAllResult);
    }

    private UserRedeem givenDoneUserRedeem() {
        return randomUserRedeem().markDone();
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