package org.binchoo.paimonganyu.infra.redeem.web;

import org.binchoo.paimonganyu.hoyoapi.HoyoCodeRedemptionApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
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
@SpringJUnitConfig(HoyoApiWebClientConfigurer.class)
class RedeemClientAdapterIntegTest {

    @Autowired
    private HoyoCodeRedemptionApi redemptionApi;

    private RedemptionClientAdapter redeemClientAdapter;

    @BeforeEach
    void init() {
        redeemClientAdapter = new RedemptionClientAdapter(redemptionApi);
    }

    @DisplayName("다수의 리뎀션 요청을 일으킨 후 그 결과를 조인할 수 있다.")
    @Test
    void redeem() {
        var tasks = List.of(getMockTask("aaa"), getMockTask("bbb"), getMockTask("ccc"));
        var result = redeemClientAdapter.redeem(tasks, System.out::println);

        assertThat(result)
                .as("Added three tasks.").hasSize(3)
                .as("Added invalid mock tasks.").allMatch((it)-> !it.isDone());
    }

    private RedeemTask getMockTask(String id) {
        var uid = HoyopassMockUtils.getMockUid();
        return RedeemTask.builder()
                .botUserId(id)
                .credentials(HoyopassCredentials.builder()
                    .ltuid(id).ltoken(id).cookieToken(id)
                    .build())
                .uid(uid)
                .redeemCode(new RedeemCode("genshingift"))
                .build();
    }
}
