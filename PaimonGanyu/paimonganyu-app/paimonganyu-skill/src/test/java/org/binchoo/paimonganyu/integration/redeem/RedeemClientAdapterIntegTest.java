package org.binchoo.paimonganyu.integration.redeem;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.HoyoCodeRedemptionApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.infra.redeem.web.RedemptionClientAdapter;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.testfixture.hoyopass.HoyopassMockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : jbinchoo
 * @since : 2022-05-12
 */
@Slf4j
@SpringJUnitConfig(HoyoApiWebClientConfigurer.class)
class RedeemClientAdapterIntegTest {

    @Autowired
    private HoyoCodeRedemptionApi redemptionApi;

    private RedemptionClientAdapter redeemClientAdapter;

    @BeforeEach
    void initAdapter() {
        redeemClientAdapter = new RedemptionClientAdapter(redemptionApi);
    }

    @DisplayName("다수의 리뎀션 요청을 일으킨 후 그 결과를 조인할 수 있다.")
    @Test
    void redeem() {
        var tasks = List.of(getMockTask("aaa"), getMockTask("bbb"), getMockTask("ccc"));
        var result = redeemClientAdapter.redeem(tasks, it-> log.info(it.toString()));

        assertThat(result)
                .as("Added three tasks.").hasSize(3)
                .as("Added invalid mock tasks.").allMatch(it-> !it.isDone());
    }

    private RedeemTask getMockTask(String placeholder) {
        var uid = HoyopassMockUtils.mockUid();
        return RedeemTask.builder()
                .botUserId(placeholder)
                .credentials(HoyopassCredentials.builder()
                    .ltuid(placeholder).ltoken(placeholder).cookieToken(placeholder)
                    .build())
                .uid(uid)
                .redeemCode(new RedeemCode("genshingift"))
                .build();
    }
}
