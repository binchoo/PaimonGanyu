package org.binchoo.paimonganyu.service.dailycheck;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityZeroException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class DailyCheckService implements DailyCheckPort {

    private final DailyCheckClientPort dailyCheckClient;
    private final UserDailyCheckCrudPort repository;

    @Override
    public List<UserDailyCheck> claimDailyCheckIn(UserHoyopass userHoyopass) {
        String botUserId = userHoyopass.getBotUserId();
        return userHoyopass.getHoyopasses().stream()
                .map(pass-> claimDailyCheckIn(botUserId, pass))
                .collect(Collectors.toList());
    }

    @Override
    public UserDailyCheck claimDailyCheckIn(String botUserId, Hoyopass pass) {
        String ltuid = pass.getLtuid();
        String ltoken = pass.getLtoken();
        return claimDailyCheckIn(botUserId, ltuid, ltoken);
    }

    @Override
    public UserDailyCheck claimDailyCheckIn(String botUserId, String ltuid, String ltoken) {
        UserDailyCheck startState = initialize(botUserId, ltuid, ltoken);
        UserDailyCheck finalState = startState.doRequest(dailyCheckClient);
        log.debug("Saving a UserDailyCheck: {}", finalState);
        return save(finalState);
    }

    // TODO: initialState를 DB에 선저장 할지 말지 결정하라.
    private UserDailyCheck initialize(String botUserId, String ltuid, String ltoken) {
        return UserDailyCheck.of(botUserId, ltuid, ltoken);
    }

    private UserDailyCheck save(UserDailyCheck userDailyCheck) {
        return repository.save(userDailyCheck);
    }

    @Override
    public List<List<UserDailyCheck>> historyOfUser(UserHoyopass user, int count) {
        String botUserId = user.getBotUserId();
        List<List<UserDailyCheck>> logs = user.getHoyopasses().stream()
                .map(pass-> historyOfUser(botUserId, pass, count))
                .collect(Collectors.toList());
        return checkNotEmpty(user, logs);
    }

    private List<List<UserDailyCheck>> checkNotEmpty(UserHoyopass user, List<List<UserDailyCheck>> logs) {
        long logCount =  logs.stream().flatMap(List::stream).count();
        if (logCount <= 0)
            throw new QuantityZeroException(user);
        return logs;
    }

    @Override
    public List<UserDailyCheck> historyOfUser(String botUserId, Hoyopass pass, int count) {
        String ltuid = pass.getLtuid();
        return repository.findByBotUserIdLtuid(botUserId, ltuid).stream()
                .filter(it-> !it.isInitialState())
                .sorted().limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasCheckedIn(String botUserId, String ltuid, LocalDate date) {
        return repository
                .findByBotUserIdLtuid(botUserId, ltuid).stream()
                .anyMatch(userDailyCheck-> userDailyCheck.isDoneOn(date));
    }

    @Override
    public boolean hasCheckedInToday(String botUserId, String ltuid) {
        return hasCheckedIn(botUserId, ltuid, LocalDate.now());
    }
}
