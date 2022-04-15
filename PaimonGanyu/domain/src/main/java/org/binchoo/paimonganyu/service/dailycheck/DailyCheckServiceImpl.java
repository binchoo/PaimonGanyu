package org.binchoo.paimonganyu.service.dailycheck;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class DailyCheckServiceImpl implements DailyCheckService {

    private final DailyCheckClientPort dailyCheckClientPort;
    private final UserDailyCheckCrudPort repository;

    public void claimDailyCheckIn(String botUserId, String ltuid, String ltoken) {
        final UserDailyCheck userDailyCheck = repository.save(UserDailyCheck.getInitialized(botUserId, ltuid, ltoken));
        final UserDailyCheck statusUpdated = userDailyCheck.doRequest(dailyCheckClientPort);
        saveFinal(statusUpdated);
    }

    private void saveFinal(UserDailyCheck userDailyCheck) {
        repository.save(userDailyCheck);
        log.info("Saved: {}", userDailyCheck);
    }

    public boolean hasCheckedInToday(String botUserId, String ltuid) {
        LocalDate today = LocalDate.now();
        return this.hasCheckedIn(botUserId, ltuid, today);
    }

    public boolean hasCheckedIn(String botUserId, String ltuid, LocalDate date) {
        return repository.findByBotUserIdLtuid(botUserId, ltuid)
                .stream().anyMatch(userDailyCheck-> userDailyCheck.isDoneOn(date));
    }
}