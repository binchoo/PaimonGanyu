package org.binchoo.paimonganyu.service.redeem;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.junit.jupiter.api.BeforeEach;
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
@ExtendWith(MockitoExtension.class)
class CodeRedeemBloomFilterServiceTest {

    @Mock
    RedeemCode mockRedeemCode;

    @Mock
    UserRedeemCrudPort userRedeemCrudPort;

    UserRedeemBloomFilterService codeRedeemSnapshotBloomFilter;
    UserRedeem userRedeem;

    @BeforeEach
    public void init() {
        codeRedeemSnapshotBloomFilter = new UserRedeemBloomFilterService(userRedeemCrudPort);
        userRedeem = new UserRedeem("user", "ltuid", mockRedeemCode);
    }

    @Test
    void givenEmptyHistories_hasRedeemed_returnsFalse() {
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode)).willReturn(Collections.emptyList());

        boolean hasRedeemed = codeRedeemSnapshotBloomFilter.hasRedeemed("foo", "bar", mockRedeemCode);

        assertThat(hasRedeemed).isFalse();
    }

    @Test
    void givenEmptyHistories_hasNotRedeemed_returnsTrue() {
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode)).willReturn(Collections.emptyList());

        boolean hasNotRedeemed = codeRedeemSnapshotBloomFilter.hasNotRedeemed("foo", "bar", mockRedeemCode);

        assertThat(hasNotRedeemed).isTrue();
    }

    @Test
    void givenComplementHistories_hasRedeemed_returnsFalse() {
        given(mockRedeemCode.getCode()).willReturn("foobarcode");
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode))
                .willReturn(complementSet(userRedeem, 100000));
        lenient().when(userRedeemCrudPort.existMatches(userRedeem))
                .thenReturn(false);

        boolean hasRedeemed = codeRedeemSnapshotBloomFilter
                .hasRedeemed(userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode());

        assertThat(hasRedeemed).isFalse();
    }

    private List<UserRedeem> complementSet(UserRedeem userRedeem, int len) {
        return IntStream.range(0, len).mapToObj(it-> {
                    String random = RandomString.make();
                    return new UserRedeem(random, random, userRedeem.getRedeemCode());
                })
                .filter(it-> !it.equals(userRedeem))
                .collect(Collectors.toList());
    }

    @Test
    void givenInclusiveHistories_hasRedeemed_callExistMatches() {
        given(mockRedeemCode.getCode()).willReturn("foobarcode");
        given(userRedeemCrudPort.findByRedeemCode(mockRedeemCode))
                .willReturn(inclusiveSet(userRedeem, 100000));

        codeRedeemSnapshotBloomFilter
                .hasRedeemed(userRedeem.getBotUserId(), userRedeem.getLtuid(), userRedeem.getRedeemCode());

        verify(userRedeemCrudPort).existMatches(userRedeem);
    }

    private List<UserRedeem> inclusiveSet(UserRedeem userRedeem, int len) {
        final int includeAt = Math.abs(new Random().nextInt()) % len;
        return IntStream.range(0, len).mapToObj(it-> {
                    String random = RandomString.make();
                    if (includeAt == it) {
                        System.out.println("Inserted at " + it);
                        return userRedeem;
                    }
                    return new UserRedeem(random, random, userRedeem.getRedeemCode());
                })
                .collect(Collectors.toList());
    }
}