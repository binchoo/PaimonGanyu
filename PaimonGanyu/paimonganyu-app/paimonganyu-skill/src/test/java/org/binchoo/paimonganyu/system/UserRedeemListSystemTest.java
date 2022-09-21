package org.binchoo.paimonganyu.system;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.chatbot.PaimonGanyu;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driving.RedeemHistoryPort;
import org.binchoo.paimonganyu.testconfig.TestAmazonClientsConfig;
import org.binchoo.paimonganyu.testconfig.TestHoyopassCredentialsConfig;
import org.binchoo.paimonganyu.testconfig.TestUserRedeemTableSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jbinchoo
 * @since 2022/09/17
 */
@Slf4j
@SpringBootTest(classes = {
        TestAmazonClientsConfig.class, TestHoyopassCredentialsConfig.class, TestUserRedeemTableSetup.class,
        PaimonGanyu.class})
public class UserRedeemListSystemTest {

    @Autowired
    RedeemHistoryPort redeemHistory;

    @DisplayName("테스트 자료 인입이 확인된다.")
    @Test
    public void verifyTableContentExist() {
        List<UserRedeem> items = redeemHistory.findAll();

        assertThat(items).isNotEmpty();
        log.info("Test items: {}", items);
    }
//
//    @DisplayName("최신 n건의 리딤 이력을 조회할 수 있다.")
//    @Test
//    public void givenLimit_findByUser_returnsTopLatestItems() {
//        int limit = 4;
//        List<UserRedeem> queryResult = redeemHistory.findByUser(
//                HoyopassMockUtils.mockUserHoyopass("testuser-3"), limit);
//
//        assertThat(queryResult).hasSizeLessThanOrEqualTo(limit);
//        assertThat(queryResult).containsSequence(queryResult.stream().sorted(Comparator.comparing(UserRedeem::getDate).reversed()).collect(Collectors.toList()));
//        log.info("Top lasted {} items: {}", limit, queryResult);
//    }
}
