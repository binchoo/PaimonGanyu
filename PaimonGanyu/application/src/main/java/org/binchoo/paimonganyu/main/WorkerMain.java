package org.binchoo.paimonganyu.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckService;
import org.binchoo.paimonganyu.service.dailycheck.DailyCheckServiceImpl;
import org.binchoo.paimonganyu.hoyoapi.HoyolabDailyCheckApi;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoAdapter;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.infra.dailycheck.web.DailyCheckClientAdapter;
import org.binchoo.paimonganyu.main.config.DynamoDBClientConfig;
import org.binchoo.paimonganyu.main.config.HoyoApiConfig;
import org.binchoo.paimonganyu.main.config.UserDailyCheckTableConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        HoyoApiConfig.class,
        DynamoDBClientConfig.class, UserDailyCheckTableConfig.class
})
@Configuration
public class WorkerMain {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * @param dailyCheckApi from {@link HoyoApiConfig}
     * @param repository from {@link UserDailyCheckTableConfig}
     */
    @Bean
    public DailyCheckService dailyCheckService(HoyolabDailyCheckApi dailyCheckApi,
                                               UserDailyCheckDynamoRepository repository) {
        return new DailyCheckServiceImpl(
                new DailyCheckClientAdapter(dailyCheckApi),
                new UserDailyCheckDynamoAdapter(repository));
    }
}