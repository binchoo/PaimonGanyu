package org.binchoo.paimonganyu.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.service.DailyCheckService;
import org.binchoo.paimonganyu.hoyoapi.HoyolabDailyCheckApi;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoAdapter;
import org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.infra.dailycheck.web.DailyCheckClientAdapter;
import org.binchoo.paimonganyu.lambda.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        SqsClientConfig.class, HoyoApiConfig.class,
        DynamoDBClientConfig.class, UserDailyCheckTableConfig.class
})
@Configuration
public class HitoriDailyCheckLambdaMain {

    /**
     * from {@link SqsClientConfig}
     */
    @Autowired
    private AmazonSQS sqsClient;

    /**
     * from {@link DynamoDBClientConfig}
     */
    @Autowired
    private AmazonDynamoDB dynamoDBClient;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * @param dailyCheckClientPort from this config.
     * @param repository from this config.
     * @return
     */
    @Bean
    public DailyCheckService dailyCheckService(DailyCheckClientPort dailyCheckClientPort,
                                               UserDailyCheckCrudPort repository) {
        return new DailyCheckService(dailyCheckClientPort, repository);
    }

    /**
     * @param dailyCheckApi from {@link HoyoApiConfig}
     */
    @Bean
    public DailyCheckClientPort dailyCheckClientPort(HoyolabDailyCheckApi dailyCheckApi) {
        // from HoyoApiConfig
        return new DailyCheckClientAdapter(dailyCheckApi);
    }

    /**
     * @param repository from {@link UserDailyCheckTableConfig}
     */
    @Bean
    public UserDailyCheckCrudPort userDailyCheckCrudPort(UserDailyCheckDynamoRepository repository) {
        return new UserDailyCheckDynamoAdapter(repository);
    }
}