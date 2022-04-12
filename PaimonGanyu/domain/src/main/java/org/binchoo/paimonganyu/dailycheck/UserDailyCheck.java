package org.binchoo.paimonganyu.dailycheck;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@Builder(toBuilder = true)
public class UserDailyCheck {

    private String botUserId;

    private String ltuid;

    private UserDailyCheckStatus status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public UserDailyCheck markComplete() {
        return this.changeStatus(UserDailyCheckStatus.COMPLETED);
    }

    public UserDailyCheck markFail(Throwable t) {
        log.warn("Received an exception.", t);
        return this.changeStatus(UserDailyCheckStatus.FAILED);
    }

    public UserDailyCheck markDuplicate() {
        return this.changeStatus(UserDailyCheckStatus.DUPLICATE);
    }

    private UserDailyCheck changeStatus(UserDailyCheckStatus status) {
        UserDailyCheck userDailyCheck = this.toBuilder().status(status).build();
        log.info("Marked the status as {}: {}", status, userDailyCheck);
        return userDailyCheck;
    }

    public static UserDailyCheck getInitialized(String botUserid, String ltuid) {
        return UserDailyCheck.builder().botUserId(botUserid)
                .ltuid(ltuid).status(UserDailyCheckStatus.QUEUED).build();
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
        return botUserId;
    }

    public String getLtuid() {
        return ltuid;
    }
}
