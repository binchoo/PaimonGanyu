package org.binchoo.paimonganyu.redeem;

import java.util.Arrays;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022/04/17
 */
public enum UserRedeemStatus {

    /** This coderedeem is completed successfully. */
    COMPLETED,
    /** This coderedeem has failed due to unknown errors. */
    FAILED,
    /** This coderedeem is already done, maybe by the user manually. */
    DUPLICATE,
    /** This coderedeem request will be soon processed. */
    QUEUED;

    private final static List<UserRedeemStatus> groupOfDone = Arrays.asList(COMPLETED, DUPLICATE);
    private final static List<UserRedeemStatus> groupOfNotDone = Arrays.asList(COMPLETED, DUPLICATE);

    public static List<UserRedeemStatus> groupOfDone() {
        return groupOfDone;
    }

    public static List<UserRedeemStatus> groupOfNotDone() {
        return groupOfNotDone;
    }
}
