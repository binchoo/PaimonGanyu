package org.binchoo.paimonganyu.hoyopass.infra.dynamo;

import org.binchoo.paimonganyu.hoyopass.infra.dynamo.entity.UserHoyopassItem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
interface UserHoyopassDynamoRepository extends CrudRepository<UserHoyopassItem, String> {

    List<UserHoyopassItem> findAll();
    Optional<UserHoyopassItem> findByBotUserId(String botUserId);
    UserHoyopassItem save(UserHoyopassItem entity);
    boolean existsByBotUserId(String botUserId);
    void delete(UserHoyopassItem entity);
    void deleteAll();
}