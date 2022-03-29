package org.binchoo.paimonganyu.hoyopass.infra.dynamo;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.driving.UserHoyopassRepository;
import org.binchoo.paimonganyu.hoyopass.infra.dynamo.entity.UserHoyopassTable;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserHoyopassDynamoAdapter implements UserHoyopassRepository {

    private final UserHoyopassDynamoRepository repository;

    @Override
    public Optional<UserHoyopass> findByBotUserId(String botUserId) {
        Optional<UserHoyopassTable> userHoyopass = repository.findByBotUserId(botUserId);
        return userHoyopass.map(UserHoyopassTable::toDomain);
    }

    @Override
    public UserHoyopass save(UserHoyopass entity) {
        return repository.save(UserHoyopassTable.fromDomain(entity)).toDomain();
    }

    @Override
    public void delete(UserHoyopass userHoyopass) {
        this.repository.delete(UserHoyopassTable.fromDomain(userHoyopass));
    }

    public boolean existsByBotUserId(String botUserId) {
        return this.repository.existsByBotUserId(botUserId);
    }

    @EnableScan
    interface UserHoyopassDynamoRepository extends CrudRepository<UserHoyopassTable, String> {

        Optional<UserHoyopassTable> findByBotUserId(String botUserId);
        UserHoyopassTable save(UserHoyopassTable entity);
        boolean existsByBotUserId(String botUserId);
        void delete(UserHoyopassTable entity);
    }
}
