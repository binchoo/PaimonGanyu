package org.binchoo.paimonganyu.service.dailycheck;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.driven.DailyCheckClientPort;
import org.binchoo.paimonganyu.dailycheck.driven.UserDailyCheckCrudPort;
import org.binchoo.paimonganyu.dailycheck.driving.DailyCheckPort;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class DailyCheckService implements DailyCheckPort {

    private final DailyCheckClientPort dailyCheckClientPort;
    private final UserDailyCheckCrudPort repository;

    @Override
    public Collection<UserDailyCheck> claimDailyCheckIn(UserHoyopass userHoyopass) {
        String botUserId = userHoyopass.getBotUserId();
        return userHoyopass.getHoyopasses().stream()
                .map(pass-> claimDailyCheckIn(botUserId, pass))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDailyCheck> claimDailyCheckIn(String botUserId, Hoyopass pass) {
        return this.claimDailyCheckIn(botUserId, pass.getLtuid(), pass.getLtoken());
    }

    @Override
    public Optional<UserDailyCheck> claimDailyCheckIn(String botUserId, String ltuid, String ltoken) {
        final UserDailyCheck userDailyCheck = initiateUserDailyCheck(botUserId, ltuid, ltoken);
        final UserDailyCheck statusUpdated = userDailyCheck.doRequest(dailyCheckClientPort);
        return saveFinal(statusUpdated);
    }

    private UserDailyCheck initiateUserDailyCheck(String botUserId, String ltuid, String ltoken) {
        UserDailyCheck userDailyCheck = UserDailyCheck.getInitialized(botUserId, ltuid, ltoken);
        repository.save(userDailyCheck);
        return userDailyCheck;
    }

    private Optional<UserDailyCheck> saveFinal(UserDailyCheck userDailyCheck) {
        UserDailyCheck history = repository.save(userDailyCheck);
        log.info("Saved: {}", history);
        return Optional.ofNullable(history);
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
}
