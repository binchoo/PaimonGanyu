package org.binchoo.paimonganyu.service.hoyopass;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassSyncPort;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author jbinchoo
 * @since 2022/10/20
 */
@Service
public class ProbabilisticHoyopassSync implements HoyopassSyncPort {

    public static final double DEFAULT_UPDATE_RATIO = 0.12;

    private static final Random r = new Random();

    private final UidSearchClientPort uidSearchClient;

    private double updateRatio;

    public ProbabilisticHoyopassSync(UidSearchClientPort uidSearchClient) {
        this.updateRatio = DEFAULT_UPDATE_RATIO;
        this.uidSearchClient = uidSearchClient;
    }

    @Override
    public boolean syncRequired(Hoyopass pass) {
        return r.nextDouble() < this.updateRatio;
    }

    @Override
    public boolean[] syncRequired(UserHoyopass user) {
        boolean[] toss = new boolean[user.size()];
        for (int i = 0; i < toss.length; i++)
            toss[i] = this.syncRequired(user.getHoyopassAt(i));
        return toss;
    }

    @Override
    public Hoyopass synchronize(Hoyopass pass) {
        return pass.synchronize(this.uidSearchClient);
    }

    public void setUpdateRatio(double updateRatio) {
        this.updateRatio = updateRatio;
    }

    public double getUpdateRatio() {
        return updateRatio;
    }
}
