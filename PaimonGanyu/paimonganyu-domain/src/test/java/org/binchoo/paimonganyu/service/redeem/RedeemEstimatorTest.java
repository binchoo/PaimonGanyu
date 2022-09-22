package org.binchoo.paimonganyu.service.redeem;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemDeploy;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryPort;
import org.binchoo.paimonganyu.redeem.options.RedeemTaskEstimationOption;
import org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author jbinchoo
 * @since 2022/09/19
 */
@ExtendWith(MockitoExtension.class)
class RedeemEstimatorTest {

    @Mock
    UserHoyopassCrudPort userCrud;

    @Mock
    RedeemTaskEstimationOption.RedeemDeployProvider deployProvider;

    @Mock
    RedeemHistoryPort redeemHistory;

    RedeemTaskEstimator estimator;

    @BeforeEach
    void init() {
        estimator = new RedeemTaskEstimator(redeemHistory);
    }

    @DisplayName("전 유저 대상의 리딤 코드 배포 작업 명세가 잘 생성된다.")
    @ParameterizedTest
    @MethodSource("loadProvider")
    public void givenRedeemAllUserOption_generateTasks_successfully(List<UserHoyopass> users, List<RedeemDeploy> deploys) {
        assertThat(users).hasSizeGreaterThan(0);
        assertThat(deploys).hasSizeGreaterThan(0);
        when(userCrud.findAll()).thenReturn(users);
        when(deployProvider.provide()).thenReturn(deploys);
        when(redeemHistory.hasNotRedeemed(any(), any(), any())).thenReturn(true);

        var tasks = estimator.generateTasks(new RedeemAllUsersOption(userCrud, deployProvider));

        int uids = users.stream().flatMap(it-> it.listHoyopasses().stream()).mapToInt(Hoyopass::size).sum();
        assertThat(tasks).hasSize(uids * deploys.size());
    }

    @DisplayName("전 유저 대상 리딤 코드 배포의 작업 명세는 리딤 이력이 없는 건에 한해 생성된다.")
    @ParameterizedTest
    @MethodSource("loadProvider")
    public void givenRedeemAllUserOption_generateTasks_NotRedeemed(List<UserHoyopass> users, List<RedeemDeploy> deploys) {
        assertThat(users).hasSizeGreaterThan(0);
        assertThat(deploys).hasSizeGreaterThan(0);
        when(userCrud.findAll()).thenReturn(users);
        when(deployProvider.provide()).thenReturn(deploys);
        when(redeemHistory.hasNotRedeemed(any(), any(), any())).thenReturn(false);

        var tasks = estimator.generateTasks(new RedeemAllUsersOption(userCrud, deployProvider));

        assertThat(tasks).isEmpty();
    }

    private static Stream<Arguments> loadProvider() {
        Stream.Builder<Arguments> streamBuilder = Stream.builder();
        List<RedeemDeploy> redeemDeploys = List.of(RedeemDeploy.builder()
                .code(RedeemCode.of("testcode"))
                .reason("test")
                .build());

        for (int testcase = 1; testcase <= 10; testcase++) {
            List<UserHoyopass> users = new ArrayList<>();
            for (int user = 0; user < testcase*100; user++)
                users.add(HoyopassMockUtils.mockUserHoyopass(testcase + "testuser" + user));
            streamBuilder.add(Arguments.of(users, redeemDeploys));
        }
        return streamBuilder.build();
    }
}
