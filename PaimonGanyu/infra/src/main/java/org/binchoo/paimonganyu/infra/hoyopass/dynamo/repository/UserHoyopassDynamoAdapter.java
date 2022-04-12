package org.binchoo.paimonganyu.infra.hoyopass.dynamo.repository;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.item.UserHoyopassItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserHoyopassDynamoAdapter implements UserHoyopassCrudPort {

    private final UserHoyopassDynamoRepository repository;

    @Override
    public List<UserHoyopass> findAll() {
        return repository.findAll().stream()
                .map(UserHoyopassItem::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<UserHoyopass> findByBotUserId(String botUserId) {
        Optional<UserHoyopassItem> userHoyopass = repository.findByBotUserId(botUserId);
        return userHoyopass.map(UserHoyopassItem::toDomain);
    }

    @Override
    public UserHoyopass save(UserHoyopass entity) {
        return repository.save(UserHoyopassItem.fromDomain(entity)).toDomain();
    }

    @Override
    public void delete(UserHoyopass userHoyopass) {
        this.repository.delete(UserHoyopassItem.fromDomain(userHoyopass));
    }

    @Override
    public void deleteAll() {
        this.repository.deleteAll();
    }

    public boolean existsByBotUserId(String botUserId) {
        return this.repository.existsByBotUserId(botUserId);
    }
}
