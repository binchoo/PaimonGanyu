package org.binchoo.paimonganyu.service.dailycheck;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.dailycheck.exception.NoUserDailyCheckException;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
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
    private final UserDailyCheckCrudPort dailyCheckCrud;

    @Override
    public List<UserDailyCheck> claimDailyCheckIn(UserHoyopass userHoyopass) {
        String botUserId = userHoyopass.getBotUserId();
        return userHoyopass.listHoyopasses().stream()
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
        return save(finalState);
    }

    private UserDailyCheck initialize(String botUserId, String ltuid, String ltoken) {
        return UserDailyCheck.of(botUserId, ltuid, ltoken);
    }

    private UserDailyCheck save(UserDailyCheck userDailyCheck) {
        return dailyCheckCrud.save(userDailyCheck);
    }

    @Override
    public List<List<UserDailyCheck>> historyOfUser(UserHoyopass user, int count) {
        String botUserId = user.getBotUserId();
        List<List<UserDailyCheck>> logs = user.listHoyopasses().stream()
                .map(pass-> historyOfUser(botUserId, pass, count))
                .collect(Collectors.toList());
        return checkNotEmpty(user, logs);
    }

    private List<List<UserDailyCheck>> checkNotEmpty(UserHoyopass user, List<List<UserDailyCheck>> logs) {
        long logCount = logs.stream().mapToLong(List::size).sum();
        if (logCount <= 0) throw new NoUserDailyCheckException(user);
        return logs;
    }

    @Override
    public List<UserDailyCheck> historyOfUser(String botUserId, Hoyopass pass, int count) {
        String ltuid = pass.getLtuid();
        return dailyCheckCrud.findByBotUserIdLtuid(botUserId, ltuid).stream()
                .filter(it-> !it.isInitialState()).sorted()
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasCheckedIn(String botUserId, String ltuid, LocalDate date) {
        return dailyCheckCrud.findByBotUserIdLtuid(botUserId, ltuid).stream()
                .anyMatch(userDailyCheck-> userDailyCheck.isDoneOn(date));
    }

    @Override
    public boolean hasCheckedInToday(String botUserId, String ltuid) {
        return hasCheckedIn(botUserId, ltuid, LocalDate.now());
    }

    @Override
    public double getCheckedInRate() {
        LocalDate today = LocalDate.now();
        List<UserDailyCheck> logs = dailyCheckCrud.findAllBetweenDates(today, today.plusDays(1L));
        return logs.isEmpty()? 0 : calcSuccessRate(today, logs);
    }

    private double calcSuccessRate(LocalDate date, List<UserDailyCheck> logs) {
        var group = logs.stream().collect(Collectors.groupingBy(UserDailyCheck::getBotUserId));
        int successfulUser = 0;
        for (String botUserId : group.keySet()) {
            List<UserDailyCheck> history = group.get(botUserId);
            if (history.stream().anyMatch(userDailyCheck -> userDailyCheck.isDoneOn(date))) {
                successfulUser++;
            }
        }
        log.info("success_rate date: {}", date);
        log.info("success_rate successfulUser: {}", successfulUser);
        log.info("success_rate totalUser: {}", group.size());
        return (double) successfulUser / group.size();
    }
}
