package org.binchoo.paimonganyu.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckSyncApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoAdapter;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.infra.dailycheck.web.DailyCheckClientAdapter;
import org.binchoo.paimonganyu.lambda.config.DynamoDBClientConfig;
import org.binchoo.paimonganyu.lambda.config.SqsClientConfig;
import org.binchoo.paimonganyu.lambda.config.UserDailyCheckTableConfig;
import org.binchoo.paimonganyu.service.dailycheck.DailyCheckService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        SqsClientConfig.class, HoyoApiWebClientConfigurer.class,
        DynamoDBClientConfig.class, UserDailyCheckTableConfig.class
})
@Configuration
public class DailyCheckHitoriRequesterMain {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * @param dailyCheckApi from {@link HoyoApiWebClientConfigurer}
     * @param repository from {@link UserDailyCheckTableConfig}
     */
    @Bean
    public DailyCheckPort dailyCheckService(DailyCheckSyncApi dailyCheckApi,
                                            UserDailyCheckDynamoRepository repository) {
        return new DailyCheckService(
                new DailyCheckClientAdapter(dailyCheckApi),
                new UserDailyCheckDynamoAdapter(repository));
    }
}