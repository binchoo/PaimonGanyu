package org.binchoo.paimonganyu.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.awsutils.sns.SNSEventWrapper;
import org.binchoo.paimonganyu.dailycheck.domain.DailyCheckTaskSpec;
import org.binchoo.paimonganyu.dailycheck.domain.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.infra.UserDailyCheckDynamoAdapter;
import org.binchoo.paimonganyu.hoyopass.infra.fanout.UserHoyopassMessage;

public class DailyCheckHitoriRequesterLambda {

    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private final UserDailyCheckCrudPort repositoryAdapter = new UserDailyCheckDynamoAdapter();
    private final AmazonSQS sqsClient = AmazonSQSClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handler(SNSEvent snsEvent) {
        new SNSEventWrapper(snsEvent).extractPojos(UserHoyopassMessage.class).stream()
                .map(DailyCheckTaskSpec::new)
                .filter(task-> !task.isDoneToday(repositoryAdapter))
                .forEach(task-> task.sendToQueue(sqsClient, DAILYCHECK_QUEUE_URL, objectMapper));
    }
}
