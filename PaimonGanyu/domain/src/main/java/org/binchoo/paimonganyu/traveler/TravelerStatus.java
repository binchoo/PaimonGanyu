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

    private final String uid;
    private final String name;
    private final String server;
    private final boolean lumine;
    private final int level;
    private final int currentResin;
    private final int maxResin;
    private final int currentHomeCoin;
    private final int maxHomeCoin;
    private final int currentExpeditionNum;
    private final long resinRecoverySeconds;

    public double ratioOfResin() {
        return 100. * currentResin / maxResin;
    }

    public String ratioStringOfResin() {
        return String.format("%d/%d", currentResin, maxResin);
    }

    public double ratioOfHomeCoin() {
        return 100. * currentHomeCoin / maxHomeCoin;
    }

    public String ratioStringOfHomeCoin() {
        return String.format("%d/%d", currentHomeCoin, maxHomeCoin);
    }

    public String nameFormat(String format) {
        return String.format(format, server, level, name);
    }

    public String uidFormat(String format) {
        return String.format(format, uid);
    }
}
