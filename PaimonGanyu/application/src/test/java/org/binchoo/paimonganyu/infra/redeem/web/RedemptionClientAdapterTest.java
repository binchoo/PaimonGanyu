package org.binchoo.paimonganyu.infra.redeem.web;

import org.binchoo.paimonganyu.hoyoapi.HoyoCodeRedemptionApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author jbinchoo
 * @since 2022/05/21
 */
@ExtendWith(MockitoExtension.class)
class RedemptionClientAdapterTest {

    @Mock
    RedeemTask task;

    @Mock
    HoyoCodeRedemptionApi redemptionApi;

    @InjectMocks
    RedemptionClientAdapter redemptionClientAdapter;

    @BeforeEach
    void initTask() {
        when(task.getLtuid()).thenReturn("ltuid");
        when(task.getCookieToken()).thenReturn(null);
        when(task.getUidString()).thenReturn("uid");
        when(task.getRegionString()).thenReturn(null);
        when(task.getCodeString()).thenReturn(null);
        when(task.getBotUserId()).thenReturn("botUserId");
        when(task.getRedeemCode()).thenReturn(new RedeemCode("redeemCode"));
    }

    @DisplayName("API 정상 응답이 내려오면 완수 상태의 UserRedeem을 반환한다.")
    @Test
    void givenSuccessfulRedemptionResponse_userRedeem_savedAsDone() {
        when(redemptionApi.redeem(any(), any(), any(), any()))
                .thenReturn(Mono.just(new HoyoResponse<>()));

        List<UserRedeem> userRedeems = redemptionClientAdapter.redeem(List.of(task), null);

        assertThat(userRedeems).hasSize(1);
        UserRedeem userRedeem = userRedeems.get(0);
        assertThat(userRedeem.getBotUserId()).isEqualTo("botUserId");
        assertThat(userRedeem.getUid()).isEqualTo("uid");
        assertThat(userRedeem.getRedeemCode()).isEqualTo(new RedeemCode("redeemCode"));
        assertThat(userRedeem.isDone()).isTrue();
    }

    @DisplayName("API 실패 응답이 내려오면 미완수 상태의 UserRedeem을 반환한다.")
    @Test
    void givenFailingRedemptionResponse_userRedeem_savedAsDone() {
        when(redemptionApi.redeem(any(), any(), any(), any()))
                .thenReturn(Mono.error(new Exception()));

        List<UserRedeem> userRedeems = redemptionClientAdapter.redeem(List.of(task), null);

        assertThat(userRedeems).hasSize(1);
        UserRedeem userRedeem = userRedeems.get(0);
        assertThat(userRedeem.getBotUserId()).isEqualTo("botUserId");
        assertThat(userRedeem.getUid()).isEqualTo("uid");
        assertThat(userRedeem.getRedeemCode()).isEqualTo(new RedeemCode("redeemCode"));
        assertThat(userRedeem.isDone()).isFalse();
    }
}
