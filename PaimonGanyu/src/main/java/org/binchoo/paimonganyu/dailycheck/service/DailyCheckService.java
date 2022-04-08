package org.binchoo.paimonganyu.dailycheck.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.hoyoapi.HoyolabDailyCheckApi;
import org.binchoo.paimonganyu.hoyoapi.error.exceptions.SignInException;
import org.binchoo.paimonganyu.hoyoapi.pojo.DailyCheckResult;
import org.binchoo.paimonganyu.hoyoapi.pojo.HoyoResponse;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class DailyCheckService {

    private final HoyolabDailyCheckApi dailyCheckApi;
    private final UserDailyCheckDynamoRepository userDailyCheckRepository;

    public void claimDailyCheckIn(String botUserId, String ltuid, String ltoken) {
        final UserDailyCheck userDailyCheck = queueUserDailyCheck(botUserId, ltuid);
        final UserDailyCheck statusModified = claimAndModifyStatus(userDailyCheck, ltoken);
        save(statusModified);
    }

    private UserDailyCheck queueUserDailyCheck(String botUserId, String ltuid) {
        return userDailyCheckRepository.save(UserDailyCheck.queued(botUserId, ltuid));
    }

    private UserDailyCheck claimAndModifyStatus(UserDailyCheck userDailyCheck, String ltoken) {
        try {
            HoyoResponse<DailyCheckResult> response =
                    dailyCheckApi.claimDailyCheck(new LtuidLtoken(userDailyCheck.getLtuid(), ltoken));
            log.info("Response message: {}", response.getMessage());
        } catch (SignInException e) {
            return userDailyCheck.markDuplicate();
        } catch (Exception e) {
            return userDailyCheck.markFail(e);
        }
        return userDailyCheck.markComplete();
    }

    private void save(UserDailyCheck userDailyCheck) {
        userDailyCheckRepository.save(userDailyCheck);
        log.info("saved: {}", userDailyCheck);
    }

    public boolean hasCheckedInToday(String botUserId, String ltuid) {
        LocalDate today = LocalDate.now();
        return this.hasCheckedIn(botUserId, ltuid, today);
    }

    public boolean hasCheckedIn(String botUserId, String ltuid, LocalDate date) {
        String botUserIdLtuid = this.getBotUserIdLtuid(botUserId, ltuid);
        return userDailyCheckRepository.findByBotUserIdLtuid(botUserIdLtuid)
                .stream().anyMatch(userDailyCheck-> userDailyCheck.isDoneOn(date));
    }

    private String getBotUserIdLtuid(String botUserId, String ltuid) {
        return botUserId + "-" + ltuid;
    }
}
