package org.binchoo.paimonganyu.dailycheck.domain;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.domain.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.infra.fanout.UserHoyopassMessage;

public class DailyCheckTaskSpec {

    /**
     * 일일체크 요청자의 봇 유저 아이디
     */
    String botUserId;

    /**
     * 미호요 크레덴셜
     */
    LtuidLtoken ltuidLtoken;

    public DailyCheckTaskSpec(UserHoyopassMessage message) {
        this(message.getBotUserId(), new LtuidLtoken(message.getLtuid(), message.getLtoken()));
    }

    public DailyCheckTaskSpec(String botUserId, LtuidLtoken ltuidLtoken) {
        this.botUserId = botUserId;
        this.ltuidLtoken = ltuidLtoken;
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }

    public LtuidLtoken getLtuidLtoken() {
        return ltuidLtoken;
    }

    public void setLtuidLtoken(LtuidLtoken ltuidLtoken) {
        this.ltuidLtoken = ltuidLtoken;
    }

    public boolean isDoneToday(UserDailyCheckCrudPort repositoryAdapter) {
        return false;
    }

    public String getJson(ObjectMapper objectMapper) {
        try {
          return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to serialize a DailyCheckTaskSpec", e);
        }
    }

    public void sendToQueue(AmazonSQS sqsClient, String targetQueueUrl, ObjectMapper objectMapper) {
        sqsClient.sendMessage(targetQueueUrl, this.getJson(objectMapper));
    }
}