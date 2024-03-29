package org.binchoo.paimonganyu.infra.redeem.dynamo.repository;

import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@EnableScan
public interface UserRedeemDynamoRepository extends CrudRepository<UserRedeemItem, String> {

    boolean existsByBotUserIdAndUidAndCodeAndDone(
            String botUserId, String uid, String code, boolean done);

    List<UserRedeemItem> findByBotUserIdAndUidAndCodeAndDone(
            String botUserId, String uid, String code, boolean done);

    List<UserRedeemItem> findByCode(String code);

    List<UserRedeemItem> findByBotUserId(String botUserId);

    List<UserRedeemItem> findAll();

    UserRedeemItem save(UserRedeemItem userRedeemItem);
}
