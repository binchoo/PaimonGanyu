package org.binchoo.paimonganyu.infra.dailycheck.dynamo.item;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckStatus;
import org.binchoo.paimonganyu.infra.utils.LocalDateTimeStringConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@Slf4j
@DynamoDBTable(tableName="UserDailyCheck")
public class UserDailyCheckItem {

    private static final long EXPIRY_DAYS = 7;

    @DynamoDBHashKey
    private String id;

    @DynamoDBIgnore
    private String botUserId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "botUserIdLtuid")
    private String botUserIdLtuid;

    @DynamoDBIgnore
    private String ltuid;

    @DynamoDBTypeConverted(converter = LocalDateTimeStringConverter.class)
    private LocalDateTime timestamp;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private long ttl;

    @DynamoDBTypeConvertedEnum
    private UserDailyCheckStatus status;

    public UserDailyCheckItem() { /* required by dynamodb */}

    public UserDailyCheckItem(String id, String botUserId, String ltuid, LocalDateTime timestamp, UserDailyCheckStatus status) {
        this.id = id;
        this.botUserId = botUserId;
        this.botUserIdLtuid = botUserId + "-" + ltuid;
        this.ltuid = ltuid;
        this.timestamp = timestamp;
        this.status = status;
        this.assignTtl(timestamp);
    }

    private void assignTtl(LocalDateTime timestamp) {
        this.ttl = timestamp.atZone(ZoneId.systemDefault())
                .plus(EXPIRY_DAYS, ChronoUnit.DAYS).toEpochSecond();
    }

    public static UserDailyCheckItem fromDomain(UserDailyCheck userDailyCheck) {
        var id = UUID.randomUUID().toString();
        var botUserId = userDailyCheck.getBotUserId();
        var ltuid = userDailyCheck.getLtuid();
        var timestamp = userDailyCheck.getTimestamp();
        var status = userDailyCheck.getStatus();
        return new UserDailyCheckItem(id, botUserId, ltuid, timestamp, status);
    }

    public static UserDailyCheck toDomain(UserDailyCheckItem item) {
        return UserDailyCheck.builder().botUserId(item.getBotUserId())
                .ltuid(item.getLtuid())
                .timestamp(item.getTimestamp())
                .status(item.getStatus())
                .build();
    }

    public String getBotUserId() {
        splitBotUserIdLtuid();
        return botUserId;
    }

    public String getLtuid() {
        splitBotUserIdLtuid();
        return ltuid;
    }

    private void splitBotUserIdLtuid() {
        if (botUserId == null || ltuid == null) {
            String[] split = botUserIdLtuid.split("-");
            assert 2 == split.length;
            this.botUserId = split[0];
            this.ltuid = split[1];
        }
    }
}
