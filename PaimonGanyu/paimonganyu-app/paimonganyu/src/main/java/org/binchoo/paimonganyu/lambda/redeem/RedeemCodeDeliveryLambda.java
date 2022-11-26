package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.s3.S3EventObjectReader;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.lambda.RedeemCodeDeliveryMain;
import org.binchoo.paimonganyu.lambda.redeem.dto.RedeemDeployDto;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationPort;
import org.binchoo.paimonganyu.service.redeem.RedeemSingleUidUserOption;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Setter
@Slf4j
public class RedeemCodeDeliveryLambda {

    private static final String CODEREDEEM_QUEUE_NAME = System.getenv("CODEREDEEM_QUEUE_NAME");

    private AmazonS3 s3Client;
    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private RedeemTaskEstimationPort taskEstimation;
    private UserHoyopassCrudPort userCrud;

    public RedeemCodeDeliveryLambda() {
        this.lookupDependencies(new AnnotationConfigApplicationContext(RedeemCodeDeliveryMain.class));
    }

    private void lookupDependencies(GenericApplicationContext context) {
        this.s3Client = context.getBean(AmazonS3.class);
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.taskEstimation = Objects.requireNonNull(context.getBean(RedeemTaskEstimationPort.class));
        this.userCrud = Objects.requireNonNull(context.getBean(UserHoyopassCrudPort.class));
    }

    public void handler(S3Event s3Event) {
        var eventParser = new S3EventObjectReader(s3Client);
        var redeemDeployList = eventParser.extractPojos(s3Event, RedeemDeployDto.class);
        var redeemOption = new RedeemSingleUidUserOption(userCrud,
                ()-> redeemDeployList.stream().map(RedeemDeployDto::toDomain).collect(Collectors.toUnmodifiableList()));
        sendToQueue(taskEstimation.generateTasks(redeemOption));
    }

    private void sendToQueue(List<RedeemTask> redeemTasks) {
        for (var batch : taskSplit(redeemTasks, 10))
            sqsClient.sendMessageBatch(CODEREDEEM_QUEUE_NAME, batch);
    }

    protected List<List<SendMessageBatchRequestEntry>> taskSplit(List<RedeemTask> tasks, int batchSize) {
        LinkedList<RedeemTask> taskQueue = new LinkedList<>(tasks);
        List<List<SendMessageBatchRequestEntry>> batches = new ArrayList<>();
        while (taskQueue.size() > 0) {
            List<SendMessageBatchRequestEntry> batch = new ArrayList<>();

            int fetch = Math.min(batchSize, taskQueue.size());
            while (fetch-- > 0) {
                RedeemTask task = taskQueue.removeFirst();
                String uniqueId = UUID.randomUUID().toString();
                SendMessageBatchRequestEntry entry = new SendMessageBatchRequestEntry(uniqueId, task.getJson(objectMapper));
                batch.add(entry);
            }
            batches.add(batch);
        }
        return batches;
    }
}
