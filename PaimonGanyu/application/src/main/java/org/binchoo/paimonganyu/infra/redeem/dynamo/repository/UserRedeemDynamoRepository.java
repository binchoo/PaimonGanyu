package org.binchoo.paimonganyu.infra.redeem.dynamo.repository;

import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@EnableScan
public interface UserRedeemDynamoRepository extends CrudRepository<UserRedeemItem, String> {

    boolean existsByBotUserIdAndLtuidAndCodeAndDone(
            String botUserId, String ltuid, String code, boolean done);

    List<UserRedeemItem> findByBotUserIdAndLtuidAndCodeAndDone(
            String botUserId, String ltuid, String code, boolean done);

    List<UserRedeemItem> findByCode(String code);

    List<UserRedeemItem> findAll();

    UserRedeemItem save(UserRedeemItem userRedeemItem);
}
