package org.binchoo.paimonganyu.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.binchoo.paimonganyu.dailycheck.app.DailyCheckApp;
import org.binchoo.paimonganyu.dailycheck.domain.DailyCheckTaskSpec;
import org.binchoo.paimonganyu.dailycheck.domain.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.domain.driving.DailyCheckPort;
import org.binchoo.paimonganyu.dailycheck.infra.UserDailyCheckRepository;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.infra.fanout.UserHoyopassFanoutReactor;

public class DailyCheckHitoriRequesterLambda {

    private static final String DAILYCHECK_QUEUE_URL = System.getenv("DAILYCHECK_QUEUE_URL");

    private final UserDailyCheckCrudPort logSource = new UserDailyCheckRepository();
    private final DailyCheckPort dailyCheckPort = new DailyCheckApp(logSource);
    private final UserHoyopassFanoutReactor messageReactor = new UserHoyopassFanoutReactor((message)-> {
        dailyCheckPort.request(new DailyCheckTaskSpec(
                message.getBotUserId(), new LtuidLtoken(message.getLtuid(), message.getLtoken())));
    });

    public void handler(SNSEvent snsEvent) {
        messageReactor.reactAll(snsEvent);
    }
}
