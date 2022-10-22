package org.binchoo.paimonganyu.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyoapi.DailyCheckAsyncApi;
import org.binchoo.paimonganyu.hoyoapi.autoconfig.HoyoApiWebClientConfigurer;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoAdapter;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.infra.dailycheck.web.RetryDailyCheckClientAdapter;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository.UserHoyopassDynamoAdapter;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository.UserHoyopassDynamoRepository;
import org.binchoo.paimonganyu.lambda.config.*;
import org.binchoo.paimonganyu.service.dailycheck.DailyCheckService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        SqsClientConfig.class, CloudWatchClientConfig.class, HoyoApiWebClientConfigurer.class,
        DynamoDBClientConfig.class, UserHoyopassTableConfig.class, UserDailyCheckTableConfig.class
})
@Configuration
public class DailyCheckBatchRequesterMain {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * @param dailyCheckApi from {@link HoyoApiWebClientConfigurer}
     * @param repository from {@link UserDailyCheckTableConfig}
     */
    @Bean
    public DailyCheckPort dailyCheckService(DailyCheckAsyncApi dailyCheckApi,
                                            UserDailyCheckDynamoRepository repository) {
        return new DailyCheckService(
                new RetryDailyCheckClientAdapter(dailyCheckApi),
                new UserDailyCheckDynamoAdapter(repository));
    }

    /**
     * @param repository from {@link UserHoyopassTableConfig}
     */
    @Bean
    public UserHoyopassCrudPort userHoyopassCrudPort(UserHoyopassDynamoRepository repository) {
        return new UserHoyopassDynamoAdapter(repository);
    }
}