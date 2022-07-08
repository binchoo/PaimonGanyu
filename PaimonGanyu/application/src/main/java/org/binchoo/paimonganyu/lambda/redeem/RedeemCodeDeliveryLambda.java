package org.binchoo.paimonganyu.lambda.redeem;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.AwsEventParser;
import org.binchoo.paimonganyu.awsutils.support.template.AsyncEventWrappingLambda;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.lambda.RedeemCodeDeliveryMain;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.RedeemTask;
import org.binchoo.paimonganyu.redeem.driving.RedeemTaskEstimationPort;
import org.binchoo.paimonganyu.service.redeem.RedeemAllUsersOption;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.*;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
public class RedeemCodeDeliveryLambda extends AsyncEventWrappingLambda<S3Event> {

    private static final String CODEREDEEM_QUEUE_NAME = System.getenv("CODEREDEEM_QUEUE_NAME");

    private AmazonS3 s3Client;
    private AmazonSQS sqsClient;
    private ObjectMapper objectMapper;
    private RedeemTaskEstimationPort taskEstimation;
    private UserHoyopassCrudPort userCrud;

    @Override
    protected void lookupDependencies() {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(RedeemCodeDeliveryMain.class);
        this.s3Client = context.getBean(AmazonS3.class);
        this.sqsClient = context.getBean(AmazonSQS.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
        this.taskEstimation = Objects.requireNonNull(context.getBean(RedeemTaskEstimationPort.class));
        this.userCrud = Objects.requireNonNull(context.getBean(UserHoyopassCrudPort.class));
    }

    @Override
    protected Object[] getConstructorArgs() {
        return new Object[] { s3Client };
    }

    @Override
    protected void doHandle(S3Event event, AwsEventParser<S3Event> eventParser) {
        var redeemCodeList = eventParser.extractPojos(event, RedeemCode.class);
        List<RedeemTask> tasks = taskEstimation.generateTasks(new RedeemAllUsersOption(userCrud,
                ()-> Collections.unmodifiableList(redeemCodeList)));
        sendToQueue(tasks);
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
