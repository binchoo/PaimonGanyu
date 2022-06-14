package org.binchoo.paimonganyu.infra.dailycheck.dynamo.repository;

import org.binchoo.paimonganyu.infra.dailycheck.dynamo.item.UserDailyCheckItem;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

import java.util.List;

@EnableScan
public interface UserDailyCheckDynamoRepository
        extends DynamoDBPagingAndSortingRepository<UserDailyCheckItem, String> {

    List<UserDailyCheckItem> findByBotUserIdLtuid(String botUserIdLtuid);
}