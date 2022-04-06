package org.binchoo.paimonganyu.dailycheck.service;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheckStatus;
import org.binchoo.paimonganyu.dailycheck.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.testconfig.TestAccountConfig;
import org.binchoo.paimonganyu.testconfig.dailycheck.DailyCheckIntegrationConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {DailyCheckIntegrationConfig.class, TestAccountConfig.class})
class DailyCheckServiceTest {

    @Autowired
    @Qualifier("validHoyopass")
    LtuidLtoken validHoyopass;

    @Autowired
    DailyCheckService dailyCheckService;

    @Autowired
    UserDailyCheckDynamoRepository repository;

    @Test
    public void withInvalidHoyopass_claimDailyCheckIn_savesFailedUserDailyCheck() {
        String botUserId = RandomString.make();
        String ltuid = RandomString.make();

        dailyCheckService.claimDailyCheckIn(botUserId, ltuid, "foobar");

        List<UserDailyCheck> findResult = repository.findByBotUserIdLtuid(botUserId + "-" + ltuid);
        assertThat(findResult.size()).isEqualTo(1);

        UserDailyCheck saved = findResult.get(0);
        assertThat(saved.getBotUserId()).isEqualTo(botUserId);
        assertThat(saved.getLtuid()).isEqualTo(ltuid);
        assertThat(saved.getStatus()).isEqualTo(UserDailyCheckStatus.FAILED);

        System.out.println(saved);
    }

    @Disabled("Integration test for real daily check-in is only meaningful once a day.")
    @Test
    public void withInvalidHoyopass_claimDailyCheckIn_savesCompledtedUserDailyCheck() {
        String botUserId = RandomString.make();
        String ltuid = validHoyopass.getLtuid();

        dailyCheckService.claimDailyCheckIn(botUserId, ltuid, validHoyopass.getLtoken());

        List<UserDailyCheck> findResult = repository.findByBotUserIdLtuid(botUserId + "-" + ltuid);
        assertThat(findResult.size()).isEqualTo(1);

        UserDailyCheck saved = findResult.get(0);
        assertThat(saved.getBotUserId()).isEqualTo(botUserId);
        assertThat(saved.getLtuid()).isEqualTo(ltuid);
        assertThat(saved.getStatus()).isEqualTo(UserDailyCheckStatus.COMPLETED);

        System.out.println(saved);
    }
}