package org.binchoo.paimonganyu.dailycheck;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.binchoo.paimonganyu.dailycheck.app.DailyCheckApp;
import org.binchoo.paimonganyu.dailycheck.domain.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.domain.driving.DailyCheckPort;
import org.binchoo.paimonganyu.dailycheck.infra.UserDailyCheckDynamoAdapter;

public class DailyCheckWorkerLambda {

    private final UserDailyCheckCrudPort repositoryAdapter = new UserDailyCheckDynamoAdapter();
    private final DailyCheckPort dailyCheckApplication = new DailyCheckApp(repositoryAdapter);

    public void handler(SQSEvent event) {

    }
}
