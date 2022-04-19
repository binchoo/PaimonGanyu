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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@DisplayName("[코드리딤 이력 서비스] 블룸필터 구현체")
@ExtendWith(MockitoExtension.class)
class RedeemBloomFilterServiceTest {

    @Mock
    RedeemCode mockRedeemCode;

    @Mock
    UserRedeemCrudPort userRedeemCrudPort;

    RedeemBloomFilterService redeemBloomFilterService;
    UserRedeem userRedeem;

    @BeforeEach
    public void init() {
        redeemBloomFilterService = new RedeemBloomFilterService(userRedeemCrudPort);
        userRedeem = new UserRedeem("user", "ltuid", mockRedeemCode, true);
    }

    @DisplayName("매칭 이력이 안 내려오면, 이력 완수 여부는 거짓이다")
    @Test
    void givenEmptyHistories_hasRedeemed_returnsFalse() {
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode)).willReturn(Collections.emptyList());

        boolean hasRedeemed = redeemBloomFilterService.hasRedeemed("foo", "bar", mockRedeemCode);

        assertThat(hasRedeemed).isFalse();
    }

    @DisplayName("매칭 이력 안 내려오면, 이력 미완수 여부는 참이다")
    @Test
    void givenEmptyHistories_hasNotRedeemed_returnsTrue() {
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode)).willReturn(Collections.emptyList());

        boolean hasNotRedeemed = redeemBloomFilterService.hasNotRedeemed("foo", "bar", mockRedeemCode);

        assertThat(hasNotRedeemed).isTrue();
    }

    @DisplayName("여집합이 내려오면, 이력 완수 여부는 거짓이다")
    @Test
    void givenComplementHistories_hasRedeemed_returnsFalse() {
        given(mockRedeemCode.getCode()).willReturn("foobarcode");
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode))
                .willReturn(complementSet(userRedeem, 100000));
        lenient().when(userRedeemCrudPort.existMatches(userRedeem))
                .thenReturn(false);

        boolean hasRedeemed = redeemBloomFilterService
                .hasRedeemed(userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode());

        assertThat(hasRedeemed).isFalse();
    }

    private List<UserRedeem> complementSet(UserRedeem userRedeem, int len) {
        Random r = new Random();
        return IntStream.range(0, len).mapToObj(it-> {
                    String random = RandomString.make();
                    return new UserRedeem(random, random, userRedeem.getRedeemCode(), r.nextBoolean());
                })
                .filter(it-> !it.equals(userRedeem))
                .collect(Collectors.toList());
    }

    @DisplayName("포함집합이 내려오면, 블룸필터는 참을 반환하니, 포트에 쿼리를 날리게 될 것이다")
    @Test
    void givenInclusiveHistories_hasRedeemed_callExistMatches() {
        given(mockRedeemCode.getCode()).willReturn("foobarcode");
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode))
                .willReturn(inclusiveSet(userRedeem, 100000));

        redeemBloomFilterService
                .hasRedeemed(userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode());

        verify(userRedeemCrudPort).existMatches(userRedeem);
    }

    private List<UserRedeem> inclusiveSet(UserRedeem userRedeem, int len) {
        final int includeAt = Math.abs(new Random().nextInt()) % len;
        Random r = new Random();
        return IntStream.range(0, len).mapToObj(it-> {
                    String random = RandomString.make();
                    if (includeAt == it) {
                        System.out.println("Inserted at " + it);
                        return userRedeem;
                    }
                    return new UserRedeem(random, random, userRedeem.getRedeemCode(), r.nextBoolean());
                })
                .collect(Collectors.toList());
    }
}