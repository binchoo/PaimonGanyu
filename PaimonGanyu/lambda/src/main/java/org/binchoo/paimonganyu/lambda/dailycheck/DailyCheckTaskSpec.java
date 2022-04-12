package org.binchoo.paimonganyu.lambda.dailycheck;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.lambda.hoyopass.UserHoyopassMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Getter
public class DailyCheckTaskSpec {

    /**
     * 일일체크 요청자의 봇 유저 아이디
     */
    private String botUserId;

    /**
     * 미호요 크레덴셜 아이디
     */
    private String ltuid;

    /**
     * 미호요 크레덴셜 토큰
     */
    private String ltoken;

    public DailyCheckTaskSpec() { }

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

    public String getJson(ObjectMapper objectMapper) {
        try {
          return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize a DailyCheckTaskSpec", e);
        }
    }

    public static List<DailyCheckTaskSpec> getList(UserHoyopassMessage userHoyopassMessage) {
        String botUserId = userHoyopassMessage.getBotUserId();
        return Arrays.stream(userHoyopassMessage.getLtuidLtokens())
                .map(it-> new DailyCheckTaskSpec(botUserId, it.getLtuid(), it.getLtoken()))
                .collect(Collectors.toList());
    }

    public static List<DailyCheckTaskSpec> getList(UserHoyopass userHoyopass) {
        UserHoyopassMessage userHoyopassMessage = new UserHoyopassMessage(userHoyopass);
        return DailyCheckTaskSpec.getList(userHoyopassMessage);
    }
}