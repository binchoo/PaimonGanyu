package org.binchoo.paimonganyu.infra.dailycheck.web;

import org.binchoo.paimonganyu.dailycheck.DailyCheckRequestResult;
import org.binchoo.paimonganyu.hoyoapi.HoyolabDailyCheckApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.webclient.HoyolabDailyCheckWebClient;
import org.binchoo.paimonganyu.lambda.config.HoyoApiConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author : jbinchoo
 * @since : 2022-04-13
 */
@SpringJUnitConfig({HoyoApiConfig.class})
public class DailyCheckClientAdapterIntegrationTest {

    private HoyolabDailyCheckApi dailyCheckApi = new HoyolabDailyCheckWebClient();

    private DailyCheckClientAdapter dailyCheckClientAdapter = new DailyCheckClientAdapter(dailyCheckApi);

    @Test
    void givenNoError_sendRequest_setResultCompleted() {
        LtuidLtoken duplicatedDailyCheckUser = givenValidDailyCheckUser();

        DailyCheckRequestResult result = dailyCheckClientAdapter.sendRequest(
                duplicatedDailyCheckUser.getLtuid(), duplicatedDailyCheckUser.getLtoken());
        assertThat(result.hasFailed()).isFalse();
        assertThat(result.isDuplicated()).isFalse();
        assertThat(result.getError()).isNull();
        System.out.println(result.getMessage());
    }

    private LtuidLtoken givenValidDailyCheckUser() {
        return null;
    }
}
