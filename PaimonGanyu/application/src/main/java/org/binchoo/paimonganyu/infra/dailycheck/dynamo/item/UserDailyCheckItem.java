package org.binchoo.paimonganyu.infra.dailycheck.dynamo.item;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckStatus;
import org.binchoo.paimonganyu.infra.utils.LocalDateTimeStringConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@ToString
@Setter // required for conversion and unconversion, never remove this.
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName="UserDailyCheck")
public class UserDailyCheckItem {

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

    @DynamoDBTypeConvertedEnum
    private UserDailyCheckStatus status;

    public static UserDailyCheckItem fromDomain(UserDailyCheck userDailyCheck) {
        var botUserId = userDailyCheck.getBotUserId();
        var ltuid = userDailyCheck.getLtuid();
        var botUserIdLtuid = botUserId + "-" + ltuid;
        var timestamp = userDailyCheck.getTimestamp();
        var status = userDailyCheck.getStatus();
        return new UserDailyCheckItem(UUID.randomUUID().toString(),
                botUserId, botUserIdLtuid, ltuid, timestamp, status);
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
