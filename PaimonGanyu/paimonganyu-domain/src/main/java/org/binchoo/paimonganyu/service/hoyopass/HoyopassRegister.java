package org.binchoo.paimonganyu.service.hoyopass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.HoyopassCredentials;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.UidSearchClientPort;
import org.binchoo.paimonganyu.hoyopass.driven.UserHoyopassCrudPort;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.hoyopass.exception.QuantityZeroException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Service
public class HoyopassRegister implements HoyopassRegisterPort {

    private final UidSearchClientPort uidSearchClient;
    private final UserHoyopassCrudPort userHoyopassCrud;

    @Override
    public UserHoyopass findUserHoyopass(String botUserId) {
        return userHoyopassCrud.findByBotUserId(botUserId)
                .orElseThrow(()-> new QuantityZeroException(null));
    }

    /**
     * @throws IllegalStateException 최대 소지 개수 이상의 통행증을 이 유저에게 등록하려 할 경우,
     * 유저에게 중복된 통행증을 등록하려 할 경우
     * @throws IllegalArgumentException 입력한 값이 실제 미호요와 상호작용 할 수 있는 통행증이 아닐 경우
     */
    @Override
    public UserHoyopass registerHoyopass(String botUserId, HoyopassCredentials credentials) {
        UserHoyopass userHoyopass = userHoyopassCrud.findByBotUserId(botUserId)
                .orElse(new UserHoyopass(botUserId));

        userHoyopass.addIncomplete(credentials, uidSearchClient);
        return userHoyopassCrud.save(userHoyopass);
    }

    @Override
    public List<Hoyopass> listHoyopasses(String botUserId) {
        return userHoyopassCrud.findByBotUserId(botUserId)
                .map(UserHoyopass::getHoyopasses)
                .filter(it-> !it.isEmpty())
                .orElseThrow(()-> new QuantityZeroException(null));
    }

    @Override
    public List<Uid> listUids(String botUserId) {
        return userHoyopassCrud.findByBotUserId(botUserId).map(UserHoyopass::listUids)
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Uid> listUids(String botUserId, int order) {
        return userHoyopassCrud.findByBotUserId(botUserId).map(userHoyopass-> userHoyopass.listUids(order))
                .orElse(new ArrayList<>());
    }

    @Override
    public void deleteHoyopass(String botUserId, int order) {
        userHoyopassCrud.findByBotUserId(botUserId)
                .ifPresent(userHoyopass-> {
                    if (userHoyopass.deleteAt(order) != null) {
                        userHoyopassCrud.save(userHoyopass);
                    }
                });
    }
}
