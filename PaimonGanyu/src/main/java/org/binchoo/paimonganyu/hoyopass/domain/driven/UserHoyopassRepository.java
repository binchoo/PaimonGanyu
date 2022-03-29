package org.binchoo.paimonganyu.hoyopass.domain.driven;

import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;

import java.util.Optional;

public interface UserHoyopassRepository {

    /**
     *
     * @param botUserId
     * @return
     */
    Optional<UserHoyopass> findByBotUserId(String botUserId);

    UserHoyopass save(UserHoyopass entity);

    /**
     *
     * @param userHoyopass
     */
    void delete(UserHoyopass userHoyopass);
}
