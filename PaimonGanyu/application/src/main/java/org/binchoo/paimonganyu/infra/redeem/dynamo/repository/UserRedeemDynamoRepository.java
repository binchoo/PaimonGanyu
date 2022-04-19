package org.binchoo.paimonganyu.infra.redeem.dynamo.repository;

import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeemStatus;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@EnableScan
public interface UserRedeemDynamoRepository extends CrudRepository<UserRedeemItem, String> {

    boolean existsByBotUserIdAndLtuidAndCodeAndStatusIn(
            String botUserId, String ltuid, String code, Collection<UserRedeemStatus> statuses);

    List<UserRedeemItem> findByBotUserIdAndLtuidAndCodeAndStatusIn(
            String botUserId, String ltuid, String code, Collection<UserRedeemStatus> statuses);

    List<UserRedeemItem> findByCode(RedeemCode redeemCode);

    List<UserRedeemItem> findAll();

    UserRedeemItem save(UserRedeemItem userRedeemItem);
}
