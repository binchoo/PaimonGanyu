package org.binchoo.paimonganyu.service.hoyopass;

import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassSyncPort;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author jbinchoo
 * @since 2022/10/20
 */
@Service
public class ProbablisticHoyopassSync implements HoyopassSyncPort {

    public static final double DEFAULT_UPDATE_RATIO = 0.12;

    private final UidSearchClientPort uidSearchClient;
    private final UserHoyopassCrudPort userHoyopassCrud;

    private double updateRatio;

    public ProbablisticHoyopassSync(UidSearchClientPort uidSearchClient, UserHoyopassCrudPort userHoyopassCrud) {
        this.updateRatio = DEFAULT_UPDATE_RATIO;
        this.uidSearchClient = uidSearchClient;
        this.userHoyopassCrud = userHoyopassCrud;
    }

    @Override
    public UserHoyopass synchronize(UserHoyopass old) {
        return new Random().nextDouble() < this.updateRatio ?
                doSynchronize(old) : old;
    }

    private UserHoyopass doSynchronize(UserHoyopass old) {
        return userHoyopassCrud.save(old.synchronize(uidSearchClient));
    }

    @Override
    public Hoyopass synchronize(Hoyopass old) {
        old.fillUids(uidSearchClient);
        return old;
    }

    public void setUpdateRatio(double updateRatio) {
        this.updateRatio = updateRatio;
    }

    public double getUpdateRatio() {
        return updateRatio;
    }
}
