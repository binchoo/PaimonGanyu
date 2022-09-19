package org.binchoo.paimonganyu.service.redeem;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@DisplayName("[코드리딤 이력 서비스] 블룸필터 구현체")
@ExtendWith(MockitoExtension.class)
class RedeemBloomFilterTest {

    @Mock
    UserRedeemCrudPort mockUserRedeemCrud;

    RedeemBloomFilter testBloomFilter;
    RedeemCode testCode;
    UserRedeem testUserRedeem;

    @BeforeEach
    void init() {
        testBloomFilter = new RedeemBloomFilter(mockUserRedeemCrud);
        testCode = RedeemCode.of("foobar");
        testUserRedeem = UserRedeem.builder()
                .botUserId("botUserId")
                .uid("uid")
                .redeemCode(testCode)
                .done(true)
                .build();
    }

    @DisplayName("매칭 이력이 안 내려오면, 이력 완수 여부는 거짓이다")
    @Test
    void givenEmptyHistories_hasRedeemed_returnsFalse() {
        given(mockUserRedeemCrud.findByRedeemCode(testCode))
                .willReturn(Collections.emptyList());

        boolean hasRedeemed = testBloomFilter.hasRedeemed("foo", "bar", testCode);

        assertThat(hasRedeemed).isFalse();
    }

    @DisplayName("매칭 이력 안 내려오면, 이력 미완수 여부는 참이다")
    @Test
    void givenEmptyHistories_hasNotRedeemed_returnsTrue() {
        given(mockUserRedeemCrud.findByRedeemCode(testCode))
                .willReturn(Collections.emptyList());

        boolean hasNotRedeemed = testBloomFilter.hasNotRedeemed("foo", "bar", testCode);

        assertThat(hasNotRedeemed).isTrue();
    }

    @DisplayName("여집합이 내려오면, 이력 완수 여부는 거짓이다")
    @Test
    void givenComplementHistories_hasRedeemed_returnsFalse() {
        given(mockUserRedeemCrud.findByRedeemCode(testCode))
                .willReturn(complementSet(100000, testUserRedeem));
        lenient().when(mockUserRedeemCrud.existMatches(testUserRedeem))
                .thenReturn(false);

        boolean hasRedeemed = testBloomFilter.hasRedeemed(
                testUserRedeem.getBotUserId(), testUserRedeem.getUid(), testUserRedeem.getRedeemCode());

        assertThat(hasRedeemed).isFalse();
    }

    private List<UserRedeem> complementSet(int len, UserRedeem userRedeem) {
        return IntStream.range(0, len).mapToObj(it-> randomize(userRedeem))
                .filter(it-> !it.equals(userRedeem))
                .collect(Collectors.toList());
    }

    private UserRedeem randomize(UserRedeem userRedeem) {
        String random = RandomString.make();
        return UserRedeem.builder()
                .botUserId(random)
                .uid(random)
                .redeemCode(userRedeem.getRedeemCode())
                .build();
    }

    @DisplayName("포함집합이 내려오면, 블룸필터는 참을 반환하니, 포트에 쿼리를 날리게 될 것이다")
    @Test
    void givenInclusiveHistories_hasRedeemed_callExistMatches() {
        given(mockUserRedeemCrud.findByRedeemCode(testCode))
                .willReturn(inclusiveSet(100000, testUserRedeem));

        testBloomFilter.hasRedeemed(
                testUserRedeem.getBotUserId(), testUserRedeem.getUid(), testUserRedeem.getRedeemCode());

        verify(mockUserRedeemCrud).existMatches(testUserRedeem);
    }

    private List<UserRedeem> inclusiveSet(int len, UserRedeem userRedeem) {
        final int includeAt = Math.abs(new Random().nextInt()) % len;
        return IntStream.range(0, len).mapToObj(it-> {
                    if (includeAt == it) {
                        System.out.println("Inserted at " + it);
                        return userRedeem;
                    }
                    return randomize(userRedeem);
                })
                .collect(Collectors.toList());
    }

    @DisplayName("블룸필터 사이즈를 별도로 설정하지 않으면, 기본 설정값을 사용한다")
    @Test
    void whenBloomFilterSizeNotConfigured_testGetBloomFilterSize_returnsDefaultValue() {
        var bloomFilterSize = testBloomFilter.getBloomFilterSize();

        assertThat(bloomFilterSize).isEqualTo(RedeemBloomFilter.DEFAULT_BLOOMFILTER_SIZE);
    }

    @DisplayName("블룸필터 사이즈를 음수나 0으로 설정했으면, 기본 설정값을 사용한다")
    @Test
    void givenBloomFilterSizeOfNonPositiveValue_testGetBloomFilterSize_returnsTheSameValue() {
        IntStream.of(-21474836, 0).forEach(myBloomFilterSize-> {
            RedeemBloomFilter newBloomFilter = new RedeemBloomFilter(myBloomFilterSize, mockUserRedeemCrud);

            var bloomFilterSize = newBloomFilter.getBloomFilterSize();

            assertThat(bloomFilterSize).isEqualTo(RedeemBloomFilter.DEFAULT_BLOOMFILTER_SIZE);
        });
    }

    @DisplayName("블룸필터 사이즈를 양수로 설정했으면, 그 설정값을 사용한다")
    @Test
    void givenBloomFilterSizeOfPositiveValue_testGetBloomFilterSize_returnsTheSameValue() {
        int myBloomFilterSize = 10000;
        RedeemBloomFilter newBloomFilter = new RedeemBloomFilter(myBloomFilterSize, mockUserRedeemCrud);

        var bloomFilterSize = newBloomFilter.getBloomFilterSize();

        assertThat(bloomFilterSize).isEqualTo(myBloomFilterSize);
    }
}
