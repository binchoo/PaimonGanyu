package org.binchoo.paimonganyu.service.traveler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.traveler.TravelerStatus;
import org.binchoo.paimonganyu.traveler.driven.GameRecordClientPort;
import org.binchoo.paimonganyu.traveler.driving.TravelerStatusPort;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author : jbinchoo
 * @since : 2022-06-14
 */
@RequiredArgsConstructor
@Service
public class BasicTravelerStatus implements TravelerStatusPort {

    private final GameRecordClientPort gameRecordClient;

    @Override
    public Collection<TravelerStatus> getCurrentStatus(UserHoyopass userHoyopass) {
        return gameRecordClient.getStatusOf(userHoyopass, null);
    }

    @Override
    public Collection<TravelerStatus> getCurrentStatus(Hoyopass pass) {
        return gameRecordClient.getStatusOf(pass, null);
    }
}
