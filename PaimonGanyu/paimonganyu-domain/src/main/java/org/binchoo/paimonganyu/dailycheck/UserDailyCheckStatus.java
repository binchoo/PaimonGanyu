package org.binchoo.paimonganyu.dailycheck;

public enum UserDailyCheckStatus {
    /** This dailycheck is completed successfully. */
    COMPLETED,
    /** This dailycheck has failed due to unknown errors. */
    FAILED,
    /** This dailycheck is already done, maybe by the user manually. */
    DUPLICATE,
    /** This dailycheck request will be soon processed. */
    QUEUED;

    public boolean isFailed() {
        return this.equals(FAILED);
    }

    public boolean isCompleted() {
        return this.equals(COMPLETED);
    }

    public boolean isDuplicate() {
        return this.equals(DUPLICATE);
    }
}
