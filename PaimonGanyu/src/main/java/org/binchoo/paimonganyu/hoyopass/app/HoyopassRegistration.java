package org.binchoo.paimonganyu.hoyopass.app;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.driven.HoyopassSearchPort;
import org.binchoo.paimonganyu.hoyopass.domain.driven.UserHoyopassRepository;
import org.binchoo.paimonganyu.hoyopass.domain.driving.HoyopassRegistryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HoyopassRegistration implements HoyopassRegistryPort {

    private final HoyopassSearchPort hoyopassSearchPort;
    private final UserHoyopassRepository userHoyopassRepository;

    @Override
    public UserHoyopass registerSecureHoyopass(String botUserId, String secureHoyopass) {
        //TODO: implement this method.
        return null;
    }

    @Override
    public UserHoyopass registerHoyopass(String botUserId, String ltuid, String ltoken) {
        UserHoyopass userHoyopass = userHoyopassRepository.findByBotUserId(botUserId)
                .orElse(new UserHoyopass(botUserId));

        userHoyopass.addHoyopass(ltuid, ltoken, hoyopassSearchPort);

        return userHoyopassRepository.save(userHoyopass);
    }

    @Override
    public List<Hoyopass> listHoyopasses(String botUserId) {
        return userHoyopassRepository.findByBotUserId(botUserId)
                .map(UserHoyopass::getHoyopasses)
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Uid> listUids(String botUserId) {
        return this.listHoyopasses(botUserId).stream().map(Hoyopass::getUids)
                .flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<Uid> listUids(String botUserId, int order) {
        Hoyopass hoyopass = this.listHoyopasses(botUserId).get(order);
        return hoyopass.getUids();
    }

    @Override
    public void deleteHoyopass(String botUserId, int order) {
        UserHoyopass userHoyopass = userHoyopassRepository.findByBotUserId(botUserId)
                .orElse(new UserHoyopass(botUserId));

        userHoyopass.deleteAt(order);

        userHoyopassRepository.save(userHoyopass);
    }
}
