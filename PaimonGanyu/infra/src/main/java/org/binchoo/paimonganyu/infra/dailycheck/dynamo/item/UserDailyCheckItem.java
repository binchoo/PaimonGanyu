package org.binchoo.paimonganyu.dailycheck.infra.dynamo.item;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@ToString
@Setter // required for conversion and unconversion, never remove this.
@Getter
@DynamoDBTable(tableName="UserDailyCheck")
public class UserDailyCheckItem {

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

    public UserDailyCheckItem() { }

    @Builder(toBuilder = true)
    protected UserDailyCheckItem(String id, String botUserId, String ltuid, UserDailyCheckStatus status) {
        this.id = id;
        this.botUserId = botUserId;
        this.ltuid = ltuid;
        this.botUserIdLtuid = botUserId + "-" + ltuid;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public UserDailyCheckItem markComplete() {
        return this.changeStatus(UserDailyCheckStatus.COMPLETED);
    }

    public UserDailyCheckItem markFail(Throwable t) {
        log.warn("Received an exception.", t);
        return this.changeStatus(UserDailyCheckStatus.FAILED);
    }

    public UserDailyCheckItem markDuplicate() {
        return this.changeStatus(UserDailyCheckStatus.DUPLICATE);
    }

    private UserDailyCheckItem changeStatus(UserDailyCheckStatus status) {
        UserDailyCheckItem userDailyCheckItem = this.toBuilder().status(status).build();
        log.info("Marked the status as {}: {}", status, userDailyCheckItem);
        return userDailyCheckItem;
    }

    public static UserDailyCheckItem queued(String botUserid, String ltuid) {
        return new UserDailyCheckItem(UUID.randomUUID().toString(), botUserid, ltuid, UserDailyCheckStatus.QUEUED);
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
