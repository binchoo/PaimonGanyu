package org.binchoo.paimonganyu.hoyopass.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.HoyopassSearchPort;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegistryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Service
class HoyopassRegistry implements HoyopassRegistryPort {

    private final HoyopassSearchPort hoyopassSearchPort;
    private final UserHoyopassCrudPort userHoyopassCrudPort;

    @Override
    public UserHoyopass registerHoyopass(String botUserId, String ltuid, String ltoken) {
        UserHoyopass userHoyopass = userHoyopassCrudPort.findByBotUserId(botUserId)
                .orElse(new UserHoyopass(botUserId));

        userHoyopass.addHoyopass(ltuid, ltoken, hoyopassSearchPort);

        return userHoyopassCrudPort.save(userHoyopass);
    }

    @Override
    public List<Hoyopass> listHoyopasses(String botUserId) {
        return userHoyopassCrudPort.findByBotUserId(botUserId)
                .map(UserHoyopass::getHoyopasses)
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Uid> listUids(String botUserId) {
        return userHoyopassCrudPort.findByBotUserId(botUserId).map(UserHoyopass::listUids)
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Uid> listUids(String botUserId, int order) {
        return userHoyopassCrudPort.findByBotUserId(botUserId).map(userHoyopass-> userHoyopass.listUids(order))
                .orElse(new ArrayList<>());
    }

    @Override
    public void deleteHoyopass(String botUserId, int order) {
        userHoyopassCrudPort.findByBotUserId(botUserId)
                .ifPresent(userHoyopass-> {
                    if (userHoyopass.deleteAt(order) != null) {
                        userHoyopassCrudPort.save(userHoyopass);
                    }
                });
    }
}
