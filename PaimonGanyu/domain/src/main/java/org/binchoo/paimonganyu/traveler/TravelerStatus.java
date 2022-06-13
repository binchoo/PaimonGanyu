package org.binchoo.paimonganyu.traveler;

import lombok.Builder;
import lombok.Data;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@Data
@Builder
public class TravelerStatus {

    private final String name;
    private final String server;
    private final long resinRecoverySeconds;
    private final int level;
    private final int currentResin;
    private final int maxResin;
    private final int currentHomeCoin;
    private final int maxHomeCoin;
    private final int currentExpeditionNum;
}
