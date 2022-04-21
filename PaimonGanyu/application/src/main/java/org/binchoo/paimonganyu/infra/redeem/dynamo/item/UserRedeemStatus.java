package org.binchoo.paimonganyu.infra.redeem.dynamo.item;

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
}
