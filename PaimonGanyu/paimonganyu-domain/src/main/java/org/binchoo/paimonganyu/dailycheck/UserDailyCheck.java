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
public class UserDailyCheck implements Comparable<UserDailyCheck> {

    private String botUserId;

    private String ltuid;

    /**
     * Note that, for security reason, ltoken is not saved to the table.
     * So this attribute is null when the item is read from the table.
     */
    private String ltoken;

    private UserDailyCheckStatus status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public boolean isDoneOn(LocalDate date) {
        return this.isDone() && this.dateEquals(date);
    }

    public boolean isDone() {
        return UserDailyCheckStatus.COMPLETED.equals(this.status)
                || UserDailyCheckStatus.DUPLICATE.equals(this.status);
    }

    private boolean dateEquals(LocalDate date) {
        return date.isEqual(this.timestamp.toLocalDate());
    }

    public UserDailyCheck doRequest(DailyCheckClientPort dailyCheckClientPort) {
        DailyCheckRequestResult requestResult = dailyCheckClientPort.sendRequest(ltuid, ltoken);
        if (requestResult.hasFailed()) {
            return this.markFail(requestResult.getError());
        } else if (requestResult.isDuplicated()) {
            return this.markDuplicate();
        } else {
            log.info("Daily check-in request result: {}", requestResult.getMessage());
            return this.markComplete();
        }
    }

    private UserDailyCheck markComplete() {
        return this.changeStatus(UserDailyCheckStatus.COMPLETED);
    }

    private UserDailyCheck markFail(Throwable t) {
        log.warn("Daily check-in throws an exception.", t);
        return this.changeStatus(UserDailyCheckStatus.FAILED);
    }

    private UserDailyCheck markDuplicate() {
        return this.changeStatus(UserDailyCheckStatus.DUPLICATE);
    }

    private UserDailyCheck changeStatus(UserDailyCheckStatus status) {
        UserDailyCheck userDailyCheck = this.toBuilder().status(status).build();
        log.info("UserDailyCheck state transfer: {}", userDailyCheck);
        return userDailyCheck;
    }

    public boolean hasStatus(UserDailyCheckStatus status) {
        return status.equals(this.status);
    }

    public static UserDailyCheck of(String botUserid, String ltuid, String ltoken) {
        return UserDailyCheck.builder()
                .status(UserDailyCheckStatus.QUEUED)
                .botUserId(botUserid)
                .ltoken(ltoken)
                .ltuid(ltuid)
                .build();
    }

    public boolean isInitialState() {
        return this.status.equals(UserDailyCheckStatus.QUEUED);
    }

    @Override
    public int compareTo(UserDailyCheck o) {
        return o.timestamp.compareTo(timestamp);
    }
}
