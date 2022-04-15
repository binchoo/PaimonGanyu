package org.binchoo.paimonganyu.service.hoyopass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.SecureHoyopass;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.driven.SigningKeyManagerPort;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegistryPort;
import org.binchoo.paimonganyu.hoyopass.driving.SecuredHoyopassRegistryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Service
public class SecuredHoyopassRegistry implements SecuredHoyopassRegistryPort {

    private final HoyopassRegistryPort delegate;
    private final SigningKeyManagerPort signingKeys;

    @Override
    public UserHoyopass registerHoyopass(String botUserId, String secureHoyopassString) {
        SecureHoyopass secureHoyopass = new SecureHoyopass(secureHoyopassString);
        secureHoyopass.decrypt(signingKeys.getPrivateKey());
        return delegate.registerHoyopass(botUserId, secureHoyopass.getLtuid(), secureHoyopass.getLtoken());
    }

    @Override
    public UserHoyopass registerHoyopass(String botUserId, String ltuid, String ltoken) {
        return delegate.registerHoyopass(botUserId, ltuid, ltoken);
    }

    @Override
    public List<Hoyopass> listHoyopasses(String botUserId) {
        return delegate.listHoyopasses(botUserId);
    }

    @Override
    public List<Uid> listUids(String botUserId) {
        return delegate.listUids(botUserId);
    }

    @Override
    public List<Uid> listUids(String botUserId, int order) {
        return delegate.listUids(botUserId, order);
    }

    @Override
    public void deleteHoyopass(String botUserId, int order) {
        delegate.deleteHoyopass(botUserId, order);
    }
}
