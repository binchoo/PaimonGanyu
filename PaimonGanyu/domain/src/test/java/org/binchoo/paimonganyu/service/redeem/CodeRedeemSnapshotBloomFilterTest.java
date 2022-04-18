package org.binchoo.paimonganyu.service.redeem;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserCodeRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserCodeRedeemCrudPort;
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
class CodeRedeemSnapshotBloomFilterTest {

    @Mock
    RedeemCode mockRedeemCode;

    @Mock
    UserCodeRedeemCrudPort userCodeRedeemCrudPort;

    CodeRedeemSnapshotBloomFilter codeRedeemSnapshotBloomFilter;
    UserCodeRedeem userCodeRedeem;

    @BeforeEach
    public void init() {
        codeRedeemSnapshotBloomFilter = new CodeRedeemSnapshotBloomFilter(userCodeRedeemCrudPort);
        userCodeRedeem = new UserCodeRedeem("user", "ltuid", mockRedeemCode);
    }

    @Test
    void givenEmptyHistories_hasRedeemed_returnsFalse() {
        given(userCodeRedeemCrudPort.findByRedeemCode(mockRedeemCode)).willReturn(Collections.emptyList());

        boolean hasRedeemed = codeRedeemSnapshotBloomFilter.hasRedeemed("foo", "bar", mockRedeemCode);

        assertThat(hasRedeemed).isFalse();
    }

    @Test
    void givenEmptyHistories_hasNotRedeemed_returnsTrue() {
        given(userCodeRedeemCrudPort.findByRedeemCode(mockRedeemCode)).willReturn(Collections.emptyList());

        boolean hasNotRedeemed = codeRedeemSnapshotBloomFilter.hasNotRedeemed("foo", "bar", mockRedeemCode);

        assertThat(hasNotRedeemed).isTrue();
    }

    @Test
    void givenComplementHistories_hasRedeemed_returnsFalse() {
        given(mockRedeemCode.getCode()).willReturn("foobarcode");
        given(userCodeRedeemCrudPort.findByRedeemCode(mockRedeemCode))
                .willReturn(complementSet(userCodeRedeem, 100000));
        lenient().when(userCodeRedeemCrudPort.existMatches(userCodeRedeem))
                .thenReturn(false);

        boolean hasRedeemed = codeRedeemSnapshotBloomFilter
                .hasRedeemed(userCodeRedeem.getBotUserId(), userCodeRedeem.getLtuid(), userCodeRedeem.getRedeemCode());

        assertThat(hasRedeemed).isFalse();
    }

    private List<UserCodeRedeem> complementSet(UserCodeRedeem userCodeRedeem, int len) {
        return IntStream.range(0, len)
                .mapToObj(it-> {
                    String random = RandomString.make();
                    return new UserCodeRedeem(random, random, userCodeRedeem.getRedeemCode());
                })
                .filter(it-> !it.equals(userCodeRedeem))
                .collect(Collectors.toList());
    }

    @Test
    void givenInclusiveHistories_hasRedeemed_callExistMatches() {
        given(mockRedeemCode.getCode()).willReturn("foobarcode");
        given(userCodeRedeemCrudPort.findByRedeemCode(mockRedeemCode))
                .willReturn(inclusiveSet(userCodeRedeem, 100000));

        codeRedeemSnapshotBloomFilter
                .hasRedeemed(userCodeRedeem.getBotUserId(), userCodeRedeem.getLtuid(), userCodeRedeem.getRedeemCode());

        verify(userCodeRedeemCrudPort).existMatches(userCodeRedeem);
    }

    private List<UserCodeRedeem> inclusiveSet(UserCodeRedeem userCodeRedeem, int len) {
        final int includeAt = Math.abs(new Random().nextInt()) % len;
        return IntStream.range(0, len)
                .mapToObj(it-> {
                    String random = RandomString.make();
                    if (includeAt == it) {
                        System.out.println("Inserted at " + it);
                        return userCodeRedeem;
                    }
                    return new UserCodeRedeem(random, random, userCodeRedeem.getRedeemCode());
                })
                .collect(Collectors.toList());
    }
}