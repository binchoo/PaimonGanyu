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
        UserDailyCheck userDailyCheck = createNewUserDailyCheck(botUserId, ltuid);
        try {
            sendRequest(ltuid, ltoken);
            userDailyCheck = userDailyCheck.markComplete();
        } catch (SignInException e) {
            log.info(e.getMessage(), e);
            userDailyCheck = userDailyCheck.markDuplicate();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            userDailyCheck = userDailyCheck.markFail();
        }
        save(userDailyCheck);
    }

    private UserDailyCheck createNewUserDailyCheck(String botUserId, String ltuid) {
        UserDailyCheck userDailyCheck = UserDailyCheck.queued(botUserId, ltuid);
        return userDailyCheckRepository.save(userDailyCheck);
    }

    private void sendRequest(String ltuid, String ltoken) {
        HoyoResponse<DailyCheckResult> response = dailyCheckApi.claimDailyCheck(new LtuidLtoken(ltuid, ltoken));
        System.out.println(response);
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
