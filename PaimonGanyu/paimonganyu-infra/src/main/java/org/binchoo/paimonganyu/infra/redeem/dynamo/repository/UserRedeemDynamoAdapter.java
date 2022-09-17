package org.binchoo.paimonganyu.infra.redeem.dynamo.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.binchoo.paimonganyu.redeem.driven.UserRedeemCrudPort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRedeemDynamoAdapter implements UserRedeemCrudPort {

    private final UserRedeemDynamoRepository repository;

    @Override
    public List<UserRedeem> findMatches(UserRedeem userRedeem) {
        var botUserId= userRedeem.getBotUserId();
        var ltuid = userRedeem.getUid();
        var code = userRedeem.getRedeemCode().getCode();
        var done = userRedeem.isDone();
        return repository.findByBotUserIdAndUidAndCodeAndDone(botUserId, ltuid, code, done).stream()
                .map(UserRedeemItem::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existMatches(UserRedeem userRedeem) {
        var botUserId= userRedeem.getBotUserId();
        var ltuid = userRedeem.getUid();
        var code = userRedeem.getRedeemCode().getCode();
        var done = userRedeem.isDone();
        return repository.existsByBotUserIdAndUidAndCodeAndDone(botUserId, ltuid, code, done);
    }

    @Override
    public List<UserRedeem> findByRedeemCode(RedeemCode redeemCode) {
        return repository.findByCode(redeemCode.getCode()).stream()
                .map(UserRedeemItem::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<UserRedeem> findAll() {
        return repository.findAll().stream()
                .map(UserRedeemItem::toDomain).collect(Collectors.toList());
    }

    @Override
    public UserRedeem save(UserRedeem userRedeem) {
        return UserRedeemItem.toDomain(
                repository.save(UserRedeemItem.fromDomain(userRedeem)));
    }

    @Override
    public List<UserRedeem> saveAll(Collection<UserRedeem> userRedeems) {
        Iterable<UserRedeemItem> entities = repository.saveAll(userRedeems.stream()
                .map(UserRedeemItem::fromDomain).collect(Collectors.toList()));
        return StreamSupport.stream(entities.spliterator(), false)
                .map(UserRedeemItem::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<UserRedeem> findByUser(UserHoyopass user) {
        return repository.findByBotUserId(user.getBotUserId()).stream()
                .map(UserRedeemItem::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<UserRedeem> findByUser(UserHoyopass user, int limit) {
        return repository.findByBotUserId(user.getBotUserId()).stream()
                .sorted(Comparator.comparing(UserRedeemItem::getDate).reversed())
                .limit(limit)
                .map(UserRedeemItem::toDomain)
                .collect(Collectors.toList());
    }
}
