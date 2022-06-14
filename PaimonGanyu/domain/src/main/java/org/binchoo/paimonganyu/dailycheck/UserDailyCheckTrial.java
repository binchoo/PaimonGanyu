package org.binchoo.paimonganyu.dailycheck;

import lombok.Data;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@Data
public class UserDailyCheckTrial {

    private final UserDailyCheck startState;
    private final UserDailyCheck finalState;

    public UserDailyCheckTrial(UserDailyCheck startState, UserDailyCheck finalState) {
        this.startState = startState.toBuilder().build();
        this.finalState = finalState.toBuilder().build();
    }

    @Override
    public String toString() {
        return "UserDailyCheckTrial{" +
                "startState=" + startState +
                ", finalState=" + finalState +
                '}';
    }
}
