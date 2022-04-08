package org.binchoo.paimonganyu.dailycheck.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.dynamo.LocalDateTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@ToString
@Setter // required for conversion and unconversion, never remove this.
@Getter
@DynamoDBTable(tableName="UserDailyCheck")
public class UserDailyCheck {

    @DynamoDBHashKey
    String id;

    @DynamoDBIgnore
    String botUserId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "botUserIdLtuid")
    String botUserIdLtuid;

    @DynamoDBIgnore
    String ltuid;

    @DynamoDBTypeConverted(converter = LocalDateTimeStringConverter.class)
    LocalDateTime timestamp;

    @DynamoDBTypeConvertedEnum
    UserDailyCheckStatus status;

    public UserDailyCheck() { }

    @Builder(toBuilder = true)
    protected UserDailyCheck(String id, String botUserId, String ltuid, UserDailyCheckStatus status) {
        this.id = id;
        this.botUserId = botUserId;
        this.ltuid = ltuid;
        this.botUserIdLtuid = botUserId + "-" + ltuid;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public UserDailyCheck markComplete() {
        return this.changeStatus(UserDailyCheckStatus.COMPLETED);
    }

    public UserDailyCheck markFail() {
        return this.changeStatus(UserDailyCheckStatus.FAILED);
    }

    public UserDailyCheck markDuplicate() {
        return this.changeStatus(UserDailyCheckStatus.DUPLICATE);
    }

    private UserDailyCheck changeStatus(UserDailyCheckStatus status) {
        UserDailyCheck userDailyCheck = this.toBuilder().status(status).build();
        log.info("marked as {}: {}", status, userDailyCheck);
        return userDailyCheck;
    }

    public static UserDailyCheck queued(String botUserid, String ltuid) {
        return new UserDailyCheck(UUID.randomUUID().toString(), botUserid, ltuid, UserDailyCheckStatus.QUEUED);
    }

    public boolean isDoneOn(LocalDate date) {
        return this.isDone() && this.dateEquals(date);
    }

    private boolean dateEquals(LocalDate date) {
        return date.isEqual(this.timestamp.toLocalDate());
    }

    private boolean isDone() {
        return UserDailyCheckStatus.COMPLETED.equals(this.status)
                || UserDailyCheckStatus.DUPLICATE.equals(this.status);
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
        if (ltuid == null || botUserId == null) {
            String[] split = botUserIdLtuid.split("-");
            assert(2 == split.length);
            this.botUserId = split[0];
            this.ltuid = split[1];
        }
    }
}