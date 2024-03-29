package org.binchoo.paimonganyu.service.hoyopass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.*;
import org.binchoo.paimonganyu.hoyopass.driven.SigningKeyManagePort;
import org.binchoo.paimonganyu.hoyopass.driving.HoyopassRegisterPort;
import org.binchoo.paimonganyu.hoyopass.driving.SecureHoyopassRegisterPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Service
public class SecureHoyopassRegister implements SecureHoyopassRegisterPort {

    private final HoyopassRegisterPort delegate;
    private final SigningKeyManagePort signingKeys;

    @Override
    public UserHoyopass findUserHoyopass(String botUserId) {
        return delegate.findUserHoyopass(botUserId);
    }

    /**
     * @throws IllegalStateException 최대 소지 개수 이상의 통행증을 이 유저에게 등록하려 할 경우,
     * 유저에게 중복된 통행증을 등록하려 할 경우, 복호화 할 수 없는 형식의 보안 통행증일 경우.
     * @throws IllegalArgumentException 입력한 값이 실제 미호요와 상호작용 할 수 있는 통행증이 아닐 경우
     */
    @Override
    public UserHoyopass registerHoyopass(String botUserId, String secureCredentialsString) {
        SecureHoyopassCredentials secureHoyopassCredentials = new SecureHoyopassCredentials(secureCredentialsString);
        secureHoyopassCredentials.decrypt(signingKeys.getPrivateKey());
        return delegate.registerHoyopass(botUserId, secureHoyopassCredentials.getCredentials());
    }

    /**
     * @throws IllegalStateException 최대 소지 개수 이상의 통행증을 이 유저에게 등록하려 할 경우,
     * 유저에게 중복된 통행증을 등록하려 할 경우
     * @throws IllegalArgumentException 입력한 값이 실제 미호요와 상호작용 할 수 있는 통행증이 아닐 경우
     */
    @Override
    public UserHoyopass registerHoyopass(String botUserId, HoyopassCredentials credentials) {
        return delegate.registerHoyopass(botUserId, credentials);
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

    @Override
    public void deleteUid(String botUserId, String uidString) {
        delegate.deleteUid(botUserId, uidString);
    }
}
