package org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository;

import org.binchoo.paimonganyu.infra.dailycheck.dynamo.item.UserDailyCheckItem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface UserDailyCheckDynamoRepository extends CrudRepository<UserDailyCheckItem, String> {

    List<UserDailyCheckItem> findByBotUserIdLtuid(String botUserIdLtuid);
    @EnableScanCount
    Page<UserDailyCheckItem> findByBotUserIdLtuid(String botUserIdLtuid, Pageable pageable);
}