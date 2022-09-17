package org.binchoo.paimonganyu.infra.redeem.dynamo.repository;

import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@EnableScan
public interface UserRedeemDynamoRepository extends PagingAndSortingRepository<UserRedeemItem, String> {

    boolean existsByBotUserIdAndUidAndCodeAndDone(
            String botUserId, String uid, String code, boolean done);

    List<UserRedeemItem> findByBotUserIdAndUidAndCodeAndDone(
            String botUserId, String uid, String code, boolean done);

    List<UserRedeemItem> findByCode(String code);

    List<UserRedeemItem> findByBotUserId(String botUserId);

    List<UserRedeemItem> findByBotUserId(String botUserId, Pageable pageable);

    List<UserRedeemItem> findAll();

    UserRedeemItem save(UserRedeemItem userRedeemItem);
}
