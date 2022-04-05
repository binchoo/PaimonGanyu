package org.binchoo.paimonganyu.dailycheck.repository;

import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheck;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface UserDailyCheckDynamoRepository extends CrudRepository<UserDailyCheck, String> {

    List<UserDailyCheck> findByBotUserIdLtuid(String botUserIdLtuid);
}