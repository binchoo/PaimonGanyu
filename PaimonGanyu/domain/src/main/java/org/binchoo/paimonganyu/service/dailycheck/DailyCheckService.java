package org.binchoo.paimonganyu.service.dailycheck;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheckTrial;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class DailyCheckService implements DailyCheckPort {

    private final DailyCheckClientPort dailyCheckClient;
    private final UserDailyCheckCrudPort repository;

    @Override
    public Collection<UserDailyCheckTrial> claimDailyCheckIn(UserHoyopass userHoyopass) {
        String botUserId = userHoyopass.getBotUserId();
        return userHoyopass.getHoyopasses().stream()
                .map(pass-> claimDailyCheckIn(botUserId, pass))
                .collect(Collectors.toList());
    }

    @Override
    public UserDailyCheckTrial claimDailyCheckIn(String botUserId, Hoyopass pass) {
        return this.claimDailyCheckIn(botUserId, pass.getLtuid(), pass.getLtoken());
    }

    @Override
    public UserDailyCheckTrial claimDailyCheckIn(String botUserId, String ltuid, String ltoken) {
        UserDailyCheck startState = createInitialState(botUserId, ltuid, ltoken);
        UserDailyCheck finalState = startState.doRequest(dailyCheckClient);
        return saveFinal(startState, finalState);
    }

    private UserDailyCheck createInitialState(String botUserId, String ltuid, String ltoken) {
        UserDailyCheck userDailyCheck = UserDailyCheck.initialState(botUserId, ltuid, ltoken);
        repository.save(userDailyCheck);
        return userDailyCheck;
    }

    @Override
    public boolean hasCheckedInToday(String botUserId, String ltuid) {
        LocalDate today = LocalDate.now();
        return this.hasCheckedIn(botUserId, ltuid, today);
    }

    @Override
    public boolean hasCheckedIn(String botUserId, String ltuid, LocalDate date) {
        return repository.findByBotUserIdLtuid(botUserId, ltuid)
                .stream().anyMatch(userDailyCheck-> userDailyCheck.isDoneOn(date));
    }

    private UserDailyCheckTrial saveFinal(UserDailyCheck startState, UserDailyCheck finalState) {
        log.info("Saved: {}", repository.save(finalState));
        return new UserDailyCheckTrial(startState, finalState);
    }
}
