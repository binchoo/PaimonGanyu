package org.binchoo.paimonganyu.dailycheck;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@Builder(toBuilder = true)
public class UserDailyCheck {

    private String botUserId;

    private String ltuid;

    /**
     * Note that, for security reason, ltoken is not saved to repository.
     * So this will be null if it's read from repository.
     */
    private String ltoken;

    private UserDailyCheckStatus status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public boolean isDoneOn(LocalDate date) {
        return this.isDone() && this.dateEquals(date);
    }

    private boolean isDone() {
        return UserDailyCheckStatus.COMPLETED.equals(this.status)
                || UserDailyCheckStatus.DUPLICATE.equals(this.status);
    }

    private boolean dateEquals(LocalDate date) {
        return date.isEqual(this.timestamp.toLocalDate());
    }

    public UserDailyCheck doRequest(DailyCheckClientPort dailyCheckClientPort) {
        DailyCheckRequestResult dailyCheckRequestResult = dailyCheckClientPort.sendRequest(ltuid, ltoken);
        if (dailyCheckRequestResult.hasFailed()) {
            return this.markFail(dailyCheckRequestResult.getError());
        } else if (dailyCheckRequestResult.isDuplicated()) {
            return this.markDuplicate();
        } else {
            log.info("DailyCheckResult message: {}", dailyCheckRequestResult.getMessage());
            return this.markComplete();
        }
    }

    private UserDailyCheck markComplete() {
        return this.changeStatus(UserDailyCheckStatus.COMPLETED);
    }

    private UserDailyCheck markFail(Throwable t) {
        log.warn("Received an exception.", t);
        return this.changeStatus(UserDailyCheckStatus.FAILED);
    }

    private UserDailyCheck markDuplicate() {
        return this.changeStatus(UserDailyCheckStatus.DUPLICATE);
    }

    private UserDailyCheck changeStatus(UserDailyCheckStatus status) {
        UserDailyCheck userDailyCheck = this.toBuilder().status(status).build();
        log.info("Marked the status as {}: {}", status, userDailyCheck);
        return userDailyCheck;
    }

    public static UserDailyCheck getInitialized(String botUserid, String ltuid, String ltoken) {
        return UserDailyCheck.builder().botUserId(botUserid)
                .ltuid(ltuid).ltoken(ltoken).status(UserDailyCheckStatus.QUEUED).build();
    }
}