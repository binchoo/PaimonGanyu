package org.binchoo.paimonganyu.hoyopass.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.SecureHoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.driven.SigningKeyManagerPort;
import org.binchoo.paimonganyu.hoyopass.domain.driving.HoyopassRegistryPort;
import org.binchoo.paimonganyu.hoyopass.domain.driving.HoyopassSecurityLayer;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Service
public class SecureHoyopassRegistration implements HoyopassSecurityLayer {

    private final HoyopassRegistryPort delegate;
    private final SigningKeyManagerPort signingKeys;

    @Override
    public UserHoyopass registerSecureHoyopass(String botUserId, String secureHoyopassString) {
        SecureHoyopass secureHoyopass = new SecureHoyopass(secureHoyopassString, signingKeys.getPrivateKey());
        secureHoyopass.decrypt();
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
