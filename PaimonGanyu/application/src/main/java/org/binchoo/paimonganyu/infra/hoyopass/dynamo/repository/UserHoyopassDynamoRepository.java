package org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository;

import org.binchoo.paimonganyu.infra.hoyopass.dynamo.item.UserHoyopassItem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface UserHoyopassDynamoRepository extends CrudRepository<UserHoyopassItem, String> {

    List<UserHoyopassItem> findAll();
    Optional<UserHoyopassItem> findByBotUserId(String botUserId);
    UserHoyopassItem save(UserHoyopassItem entity);
    boolean existsByBotUserId(String botUserId);
    void delete(UserHoyopassItem entity);
    void deleteAll();
}