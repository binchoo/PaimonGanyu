package org.binchoo.paimonganyu.lambda;

import org.binchoo.paimonganyu.hoyoapi.HoyoCodeRedemptionApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.infra.redeem.dynamo.repository.UserRedeemDynamoAdapter;
import org.binchoo.paimonganyu.infra.redeem.dynamo.repository.UserRedeemDynamoRepository;
import org.binchoo.paimonganyu.infra.redeem.web.RedemptionClientAdapter;
import org.binchoo.paimonganyu.lambda.config.DynamoDBClientConfig;
import org.binchoo.paimonganyu.lambda.config.UserRedeemTableConfig;
import org.binchoo.paimonganyu.redeem.driving.RedemptionService;
import org.binchoo.paimonganyu.service.redeem.Redeemer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@Import({
        HoyoApiWebClientConfigurer.class,
        DynamoDBClientConfig.class, UserRedeemTableConfig.class
})
@Configuration
public class RedeemWorkerMain {

    /**
     * @param redeemRepository from {@link UserRedeemTableConfig}
     * @param redemptionApi from {@link HoyoApiWebClientConfigurer}
     */
    @Bean
    public RedemptionService redemptionService(UserRedeemDynamoRepository redeemRepository,
                                               HoyoCodeRedemptionApi redemptionApi) {

        return new Redeemer(new UserRedeemDynamoAdapter(redeemRepository),
                new RedemptionClientAdapter(redemptionApi));
    }
}
