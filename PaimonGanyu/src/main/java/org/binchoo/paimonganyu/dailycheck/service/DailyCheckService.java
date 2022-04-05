package org.binchoo.paimonganyu.dailycheck.service;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheck;
import org.binchoo.paimonganyu.dailycheck.repository.UserDailyCheckDynamoRepository;
import org.binchoo.paimonganyu.hoyoapi.HoyolabDailyCheckApi;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DailyCheckService {

    private final HoyolabDailyCheckApi dailyCheckApi;
    private final UserDailyCheckDynamoRepository userDailyCheckRepository;

    public void claimDailyCheckIn(String botUserId, String ltuid, String ltoken) {
        try {
            dailyCheckApi.claimDailyCheck(new LtuidLtoken(ltuid, ltoken));
            userDailyCheckRepository.save(UserDailyCheck.completed(botUserId, ltuid));
        } catch (RetcodeException e) {
            userDailyCheckRepository.save(UserDailyCheck.failed(botUserId, ltuid));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasCheckedIn(String botUserId, String ltuid, LocalDate date) {
        return userDailyCheckRepository.findByBotUserIdLtuid(
                this.getBotUserIdLtuid(botUserId, ltuid)).stream()
                .anyMatch(chekin-> chekin.isDoneOn(date));
    }

    public boolean hasCheckedInToday(String botUserId, String ltuid) {
        LocalDate today = LocalDate.now();
        return this.hasCheckedIn(botUserId, ltuid, today);
    }

    private String getBotUserIdLtuid(String botUserId, String ltuid) {
        return botUserId + "-" + ltuid;
    }
}
