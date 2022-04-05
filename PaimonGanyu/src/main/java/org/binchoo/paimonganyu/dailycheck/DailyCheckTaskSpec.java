package org.binchoo.paimonganyu.dailycheck;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binchoo.paimonganyu.dailycheck.service.DailyCheckService;
import org.binchoo.paimonganyu.hoyopass.infra.fanout.UserHoyopassMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DailyCheckTaskSpec {

    /**
     * 일일체크 요청자의 봇 유저 아이디
     */
    String botUserId;

    /**
     * 미호요 크레덴셜 아이디
     */
    String ltuid;

    /**
     * 미호요 크레덴셜 토큰
     */
    String ltoken;

    public DailyCheckTaskSpec(String botUserId, String ltuid, String ltoken) {
        this.botUserId = botUserId;
        this.ltuid = ltuid;
        this.ltoken = ltoken;
    }

    public String getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(String botUserId) {
        this.botUserId = botUserId;
    }

    public String getLtuid() {
        return ltuid;
    }

    public void setLtuid(String ltuid) {
        this.ltuid = ltuid;
    }

    public String getLtoken() {
        return ltoken;
    }

    public void setLtoken(String ltoken) {
        this.ltoken = ltoken;
    }

    public boolean isDoneToday(DailyCheckService dailyCheckService) {
        return dailyCheckService.hasCheckedInToday(botUserId, ltuid);
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

    static List<DailyCheckTaskSpec> getList(UserHoyopassMessage userHoyopassMessage) {
        String botUserId = userHoyopassMessage.getBotUserId();
        return Arrays.stream(userHoyopassMessage.getLtuidLtokens())
                .map(it-> new DailyCheckTaskSpec(botUserId, it.getLtuid(), it.getLtoken()))
                .collect(Collectors.toList());
    }
}